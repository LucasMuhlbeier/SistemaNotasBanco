package com.example.sistemanotas.service;

import com.example.sistemanotas.dto.AlunoDisciplinaResponseDTO;
import com.example.sistemanotas.dto.LancamentoBimestralDTO; // CORREÇÃO: Importação do novo DTO
import com.example.sistemanotas.dto.LancamentoNotasDTO;
import com.example.sistemanotas.dto.MatriculaDTO;
import com.example.sistemanotas.model.Aluno;
import com.example.sistemanotas.model.AlunoDisciplina;
import com.example.sistemanotas.model.Disciplina;
import com.example.sistemanotas.model.SituacaoAluno;
import com.example.sistemanotas.repository.AlunoDisciplinaRepository;
import com.example.sistemanotas.repository.AulasDadasPresencasRepository;
import com.example.sistemanotas.repository.AulasDadasRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Optional; // Necessário para métodos de busca

@Service
public class AlunoDisciplinaService {

    private final AlunoDisciplinaRepository alunoDisciplinaRepository;
    private final AlunoService alunoService;
    private final DisciplinaService disciplinaService;
    private final AulasDadasRepository aulasDadasRepository;
    private final AulasDadasPresencasRepository aulasDadasPresencasRepository;

    public AlunoDisciplinaService(AlunoDisciplinaRepository alunoDisciplinaRepository,
                                  AlunoService alunoService,
                                  DisciplinaService disciplinaService,
                                  AulasDadasRepository aulasDadasRepository,
                                  AulasDadasPresencasRepository aulasDadasPresencasRepository) {
        this.alunoDisciplinaRepository = alunoDisciplinaRepository;
        this.alunoService = alunoService;
        this.disciplinaService = disciplinaService;
        this.aulasDadasRepository = aulasDadasRepository;
        this.aulasDadasPresencasRepository = aulasDadasPresencasRepository;
    }

    // --- MÉTODOS DE CÁLCULO ---

    /**
     * Calcula a média parcial ou final.
     * Média Parcial = (N1 + N2) / 2
     * Média Final = (Média Parcial + NF) / 2
     */
    private BigDecimal calcularMedia(BigDecimal nota1, BigDecimal nota2, BigDecimal notaFinal) {
        BigDecimal mediaParcial = null;

        // Média Parcial (P1 + P2) / 2
        if (nota1 != null && nota2 != null) {
            mediaParcial = nota1.add(nota2).divide(new BigDecimal("2"), 1, RoundingMode.HALF_UP);
        }

        // Média Final (Média Parcial + Nota Final) / 2
        if (mediaParcial != null && notaFinal != null) {
            return mediaParcial.add(notaFinal).divide(new BigDecimal("2"), 1, RoundingMode.HALF_UP);
        }

        return mediaParcial;
    }

    /**
     * Determina a situação do aluno com base na média e frequência (Regras de Negócio).
     * @param mediaFinal Média após exame (se houver).
     * @param mediaParcial Média dos bimestres.
     * @param frequencia Percentual de presença (0.00 a 100.00).
     * @return SituacaoAluno (APROVADO, REPROVADO, EM_EXAME, etc.)
     */
    private SituacaoAluno determinarSituacao(BigDecimal mediaFinal, BigDecimal mediaParcial, BigDecimal frequencia) {
        if (frequencia == null || mediaParcial == null) {
            return SituacaoAluno.CURSANDO;
        }

        // 1. REPROVADO POR FALTA (Prioridade: Frequência mínima de 75% de presença)
        // O enunciado pede 25% de presença, mas usaremos 75% de presença (prática comum)
        // O percentual de frequência é o percentual de presença
        if (frequencia.compareTo(new BigDecimal("75.0")) < 0) {
            return SituacaoAluno.REPROVADO_POR_FALTA;
        }

        // 2. APROVADO/REPROVADO APÓS EXAME FINAL (Regra: Média Final >= 5.0)
        if (mediaFinal != null) {
            // Nota: O enunciado define 6.0 como média de aprovação, mas mantemos o sistema de exame (>=5.0)
            return mediaFinal.compareTo(new BigDecimal("6.0")) >= 0 ?
                    SituacaoAluno.APROVADO : SituacaoAluno.REPROVADO_POR_NOTA;
        }

        // 3. SITUAÇÃO BASEADA APENAS NA MÉDIA PARCIAL
        // Aprovação direta (Regra: Média Parcial >= 7.0, mantendo a regra mais robusta)
        if (mediaParcial.compareTo(new BigDecimal("7.0")) >= 0) {
            return SituacaoAluno.APROVADO;
        }
        // Em Exame (Regra: Média Parcial >= 4.0 e < 7.0)
        if (mediaParcial.compareTo(new BigDecimal("4.0")) >= 0) {
            return SituacaoAluno.EM_EXAME;
        }

        // 4. REPROVADO ANTES DO EXAME (Média Parcial < 4.0)
        return SituacaoAluno.REPROVADO_POR_NOTA;
    }

    /**
     * Recalcula a média, frequência e situação da matrícula e a salva.
     * @param ad AlunoDisciplina a ser recalculada
     */
    private void recalcularEAtualizar(AlunoDisciplina ad) {
        // 1. CÁLCULO DE NOTAS
        BigDecimal nota1 = ad.getNota1Bimestre();
        BigDecimal nota2 = ad.getNota2Bimestre();
        BigDecimal notaFinal = ad.getNotaFinal();

        BigDecimal mediaParcial = calcularMedia(nota1, nota2, null);
        BigDecimal mediaFinal = calcularMedia(nota1, nota2, notaFinal);

        ad.setMediaParcial(mediaParcial);
        ad.setMediaFinal(mediaFinal);

        // 2. CÁLCULO DE FREQUÊNCIA (Sempre baseado no total de aulas dadas e faltas registradas)
        Long totalAulas = aulasDadasRepository.countByDisciplinaId(ad.getDisciplina().getId());
        Long totalFaltas = aulasDadasPresencasRepository.countTotalFaltasByAlunoAndDisciplina(
                ad.getAluno().getId(), ad.getDisciplina().getId());

        ad.setTotalAulasDadas(totalAulas);
        ad.setTotalFaltas(totalFaltas);

        BigDecimal frequencia = new BigDecimal("100.00");
        if (totalAulas != null && totalAulas > 0) {
            long presencas = totalAulas - totalFaltas;
            frequencia = new BigDecimal(presencas)
                    .multiply(new BigDecimal("100"))
                    .divide(new BigDecimal(totalAulas), 2, RoundingMode.HALF_UP);
        }
        ad.setPercentualFrequencia(frequencia);

        // 3. DETERMINAÇÃO DA SITUAÇÃO FINAL
        SituacaoAluno situacao = determinarSituacao(mediaFinal, mediaParcial, frequencia);
        ad.setSituacao(situacao);
    }

    // --- MÉTODOS DE CONVERSÃO E UTILIDADE ---

    public AlunoDisciplinaResponseDTO toResponseDTO(AlunoDisciplina ad) {
        // Recalcula e salva o estado atualizado no banco antes de retornar.
        recalcularEAtualizar(ad);
        alunoDisciplinaRepository.save(ad);

        AlunoDisciplinaResponseDTO dto = new AlunoDisciplinaResponseDTO();
        dto.setId(ad.getId());
        dto.setAlunoId(ad.getAluno().getId());
        dto.setAlunoNome(ad.getAluno().getNome());
        dto.setDisciplinaId(ad.getDisciplina().getId());
        dto.setDisciplinaCodigo(ad.getDisciplina().getCodigo());
        dto.setDisciplinaDescricao(ad.getDisciplina().getDescricao());
        dto.setNota1Bimestre(ad.getNota1Bimestre());
        dto.setNota2Bimestre(ad.getNota2Bimestre());
        dto.setNotaFinal(ad.getNotaFinal());
        dto.setMediaParcial(ad.getMediaParcial());
        dto.setMediaFinal(ad.getMediaFinal());
        dto.setTotalAulasDadas(ad.getTotalAulasDadas());
        dto.setTotalFaltas(ad.getTotalFaltas());
        dto.setPercentualFrequencia(ad.getPercentualFrequencia());
        dto.setSituacao(ad.getSituacao());
        dto.setMatriculado(ad.getMatriculado()); // Inclui o status de matrícula

        return dto;
    }

    // --- MÉTODOS DE NEGÓCIO ---

    /**
     * Matrícula de um aluno em uma disciplina.
     */
    @Transactional
    public AlunoDisciplinaResponseDTO matricular(MatriculaDTO dto) {
        Aluno aluno = alunoService.buscarEntidadePorId(dto.getAlunoId());
        Disciplina disciplina = disciplinaService.buscarEntidadePorId(dto.getDisciplinaId());

        if (alunoDisciplinaRepository.findByAlunoIdAndDisciplinaId(aluno.getId(), disciplina.getId()).isPresent()) {
            throw new RuntimeException("Aluno já matriculado nesta disciplina.");
        }

        AlunoDisciplina matricula = new AlunoDisciplina();
        matricula.setAluno(aluno);
        matricula.setDisciplina(disciplina);
        matricula.setSituacao(SituacaoAluno.CURSANDO);
        matricula.setMatriculado(true); // Nova matrícula é sempre ativa

        AlunoDisciplina matriculaSalva = alunoDisciplinaRepository.save(matricula);
        return toResponseDTO(matriculaSalva);
    }

    /**
     * Lança a nota (e nota final) e atualiza a situação após o lançamento.
     */
    @Transactional
    public AlunoDisciplinaResponseDTO lancarNotas(LancamentoNotasDTO dto) {
        AlunoDisciplina matricula = alunoDisciplinaRepository.findById(dto.getId())
                .orElseThrow(() -> new RuntimeException("Matrícula não encontrada com ID: " + dto.getId()));

        if (dto.getNota1Bimestre() != null) {
            matricula.setNota1Bimestre(dto.getNota1Bimestre());
        }
        if (dto.getNota2Bimestre() != null) {
            matricula.setNota2Bimestre(dto.getNota2Bimestre());
        }
        if (dto.getNotaFinal() != null) {
            matricula.setNotaFinal(dto.getNotaFinal());
        }

        // O recalculo e salvamento ocorrem dentro do toResponseDTO
        return toResponseDTO(matricula);
    }

    /**
     * MÉTODO DE NEGÓCIO PRINCIPAL: Lança notas e/ou faltas para um bimestre específico (1º ou 2º).
     * Atende aos endpoints PUT api/aluno/{idAluno}/disciplina/{idDisciplina}/{bimestre}
     * Aplica a Regra de Negócio de Desativação de Matrícula se for o 2º Bimestre e o aluno for APROVADO.
     */
    @Transactional
    public AlunoDisciplinaResponseDTO lancarBimestre(Long alunoId, Long disciplinaId, Integer bimestre, LancamentoBimestralDTO dto) {
        AlunoDisciplina matricula = alunoDisciplinaRepository.findByAlunoIdAndDisciplinaId(alunoId, disciplinaId)
                .orElseThrow(() -> new RuntimeException("Matrícula não encontrada para o aluno " + alunoId + " na disciplina " + disciplinaId));

        if (!matricula.getMatriculado()) {
            throw new RuntimeException("Matrícula está inativa. Não é possível lançar notas.");
        }

        // 1. ATUALIZA NOTAS
        if (bimestre == 1) {
            if (dto.getNota() != null) matricula.setNota1Bimestre(dto.getNota());
            // Nota: As faltas (dto.getFaltas()) não são salvas em AlunoDisciplina. A contagem total
            // de faltas é feita em tempo real pela tabela AulasDadasPresencas.
        } else if (bimestre == 2) {
            if (dto.getNota() != null) matricula.setNota2Bimestre(dto.getNota());
            // Nota: As faltas (dto.getFaltas()) não são salvas em AlunoDisciplina.
        } else {
            throw new IllegalArgumentException("Bimestre inválido. Use 1 ou 2.");
        }

        // 2. RECALCULAR E SALVAR (atualiza situação, média, frequência etc.)
        AlunoDisciplinaResponseDTO response = toResponseDTO(matricula);

        // 3. REGRA DE NEGÓCIO: DESATIVAR MATRÍCULA (APÓS 2º BIMESTRE)
        if (bimestre == 2) {
            // Se aprovado, desativa a matrícula (matriculado = false)
            if (matricula.getSituacao() == SituacaoAluno.APROVADO) {
                matricula.setMatriculado(false);
                alunoDisciplinaRepository.save(matricula);
                response.setMatriculado(false);
            }
            // Se reprovado, o enunciado diz para manter "true" para futura repetição/controle.
        }

        return response;
    }

    /**
     * Busca uma matrícula específica e retorna o DTO.
     */
    public AlunoDisciplinaResponseDTO buscarMatriculaPorId(Long id) {
        AlunoDisciplina matricula = alunoDisciplinaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Matrícula não encontrada com ID: " + id));

        return toResponseDTO(matricula);
    }

    /**
     * Lista todas as disciplinas cursadas por um aluno (Boletim).
     */
    public List<AlunoDisciplinaResponseDTO> listarBoletimPorAluno(Long alunoId) {
        alunoService.buscarEntidadePorId(alunoId); // Valida se o aluno existe

        List<AlunoDisciplina> matriculas = alunoDisciplinaRepository.findByAlunoId(alunoId);

        return matriculas.stream()
                .map(this::toResponseDTO) // Recalcula e salva cada matrícula antes de mapear para DTO
                .collect(Collectors.toList());
    }
}