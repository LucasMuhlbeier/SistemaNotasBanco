package com.example.sistemanotas.service;

import com.example.sistemanotas.dto.AlunoDisciplinaResponseDTO;
import com.example.sistemanotas.dto.LancamentoBimestralDTO;
import com.example.sistemanotas.dto.LancamentoNotasDTO;
import com.example.sistemanotas.dto.MatriculaDTO;
import com.example.sistemanotas.dto.NotaResponseDTO;
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
import java.util.Optional;

@Service
public class AlunoDisciplinaService {

    private final AlunoDisciplinaRepository alunoDisciplinaRepository;
    private final AlunoService alunoService;
    private final DisciplinaService disciplinaService;
    private final AulasDadasRepository aulasDadasRepository;
    private final AulasDadasPresencasRepository aulasDadasPresencasRepository;
    private final NotaService notaService;

    public AlunoDisciplinaService(AlunoDisciplinaRepository alunoDisciplinaRepository,
                                  AlunoService alunoService,
                                  DisciplinaService disciplinaService,
                                  AulasDadasRepository aulasDadasRepository,
                                  AulasDadasPresencasRepository aulasDadasPresencasRepository,
                                  NotaService notaService) {
        this.alunoDisciplinaRepository = alunoDisciplinaRepository;
        this.alunoService = alunoService;
        this.disciplinaService = disciplinaService;
        this.aulasDadasRepository = aulasDadasRepository;
        this.aulasDadasPresencasRepository = aulasDadasPresencasRepository;
        this.notaService = notaService;
    }



    private BigDecimal calcularMedia(BigDecimal nota1, BigDecimal nota2, BigDecimal notaFinal) {
        BigDecimal mediaParcial = null;

        if (nota1 != null && nota2 != null) {

            mediaParcial = nota1.add(nota2).divide(new BigDecimal("2"), 1, RoundingMode.HALF_UP);
        }

        if (mediaParcial != null && notaFinal != null) {
            return mediaParcial.add(notaFinal).divide(new BigDecimal("2"), 1, RoundingMode.HALF_UP);
        }

        return mediaParcial;
    }


    private SituacaoAluno determinarSituacao(BigDecimal mediaFinal, BigDecimal mediaParcial, BigDecimal frequencia) {
        if (frequencia == null || mediaParcial == null) {
            return SituacaoAluno.CURSANDO;
        }

        if (frequencia.compareTo(new BigDecimal("75.0")) < 0) {
            return SituacaoAluno.REPROVADO_POR_FALTA;
        }


        if (mediaFinal != null && mediaFinal.compareTo(mediaParcial) != 0) {
            return mediaFinal.compareTo(new BigDecimal("6.0")) >= 0 ?
                    SituacaoAluno.APROVADO : SituacaoAluno.REPROVADO_POR_NOTA;
        }


        if (mediaParcial.compareTo(new BigDecimal("7.0")) >= 0) {
            return SituacaoAluno.APROVADO;
        }

        if (mediaParcial.compareTo(new BigDecimal("4.0")) >= 0) {
            return SituacaoAluno.EM_EXAME;
        }

        return SituacaoAluno.REPROVADO_POR_NOTA;
    }


    private void recalcularEAtualizar(AlunoDisciplina ad) {

        BigDecimal nota1 = ad.getNota1Bimestre();
        BigDecimal nota2 = ad.getNota2Bimestre();
        BigDecimal notaFinal = ad.getNotaFinal();

        BigDecimal mediaParcial = calcularMedia(nota1, nota2, null);

        BigDecimal mediaFinal = mediaParcial;


        if (mediaParcial != null && notaFinal != null) {
            mediaFinal = mediaParcial.add(notaFinal).divide(new BigDecimal("2"), 1, RoundingMode.HALF_UP);
        }

        ad.setMediaParcial(mediaParcial);
        ad.setMediaFinal(mediaFinal);


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


        SituacaoAluno situacao = determinarSituacao(mediaFinal, mediaParcial, frequencia);
        ad.setSituacao(situacao);
    }


    @Transactional
    public AlunoDisciplinaResponseDTO toResponseDTO(AlunoDisciplina ad) {

        Long alunoId = ad.getAluno().getId();
        Long disciplinaId = ad.getDisciplina().getId();


        NotaResponseDTO nota1DTO = notaService.buscarNotaBimestral(alunoId, disciplinaId, "1bim");
        BigDecimal nota1 = nota1DTO.getNota() != null ? nota1DTO.getNota() : BigDecimal.ZERO;

        NotaResponseDTO nota2DTO = notaService.buscarNotaBimestral(alunoId, disciplinaId, "2bim");
        BigDecimal nota2 = nota2DTO.getNota() != null ? nota2DTO.getNota() : BigDecimal.ZERO;


        ad.setNota1Bimestre(nota1);
        ad.setNota2Bimestre(nota2);


        recalcularEAtualizar(ad);
        alunoDisciplinaRepository.save(ad);


        AlunoDisciplinaResponseDTO dto = new AlunoDisciplinaResponseDTO();
        dto.setId(ad.getId());
        dto.setAlunoId(ad.getAluno().getId());
        dto.setAlunoNome(ad.getAluno().getNome());
        dto.setDisciplinaId(ad.getDisciplina().getId());
        dto.setDisciplinaCodigo(ad.getDisciplina().getCodigo());
        dto.setDisciplinaDescricao(ad.getDisciplina().getDescricao());


        dto.setNota1Bimestre(ad.getNota1Bimestre().compareTo(BigDecimal.ZERO) == 0 ? null : ad.getNota1Bimestre());
        dto.setNota2Bimestre(ad.getNota2Bimestre().compareTo(BigDecimal.ZERO) == 0 ? null : ad.getNota2Bimestre());
        dto.setNotaFinal(ad.getNotaFinal());
        dto.setMediaParcial(ad.getMediaParcial());
        dto.setMediaFinal(ad.getMediaFinal());

        dto.setTotalAulasDadas(ad.getTotalAulasDadas());
        dto.setTotalFaltas(ad.getTotalFaltas());
        dto.setPercentualFrequencia(ad.getPercentualFrequencia());
        dto.setSituacao(ad.getSituacao());
        dto.setMatriculado(ad.getMatriculado());

        return dto;
    }



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
        matricula.setMatriculado(true);

        matricula.setNota1Bimestre(BigDecimal.ZERO);
        matricula.setNota2Bimestre(BigDecimal.ZERO);

        AlunoDisciplina matriculaSalva = alunoDisciplinaRepository.save(matricula);
        return toResponseDTO(matriculaSalva);
    }

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


        return toResponseDTO(matricula);
    }


    @Transactional
    public AlunoDisciplinaResponseDTO lancarBimestre(Long alunoId, Long disciplinaId, Integer bimestre, LancamentoBimestralDTO dto) {


        AlunoDisciplina matricula = alunoDisciplinaRepository.findByAlunoIdAndDisciplinaId(alunoId, disciplinaId)
                .orElseThrow(() -> new RuntimeException("Matrícula não encontrada para o aluno " + alunoId + " na disciplina " + disciplinaId));

        if (!matricula.getMatriculado()) {
            throw new RuntimeException("Matrícula está inativa. Não é possível lançar notas.");
        }


        if (bimestre == 1) {
            if (dto.getNota() != null) matricula.setNota1Bimestre(dto.getNota());

        } else if (bimestre == 2) {
            if (dto.getNota() != null) matricula.setNota2Bimestre(dto.getNota());

        } else {
            throw new IllegalArgumentException("Bimestre inválido. Use 1 ou 2.");
        }


        AlunoDisciplinaResponseDTO response = toResponseDTO(matricula);


        if (bimestre == 2) {

            if (matricula.getSituacao() == SituacaoAluno.APROVADO) {
                matricula.setMatriculado(false);
                alunoDisciplinaRepository.save(matricula);
                response.setMatriculado(false);
            }

        }

        return response;
    }


    public AlunoDisciplinaResponseDTO buscarMatriculaPorId(Long id) {
        AlunoDisciplina matricula = alunoDisciplinaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Matrícula não encontrada com ID: " + id));

        return toResponseDTO(matricula);
    }


    public List<AlunoDisciplinaResponseDTO> listarBoletimPorAluno(Long alunoId) {
        alunoService.buscarEntidadePorId(alunoId);

        List<AlunoDisciplina> matriculas = alunoDisciplinaRepository.findByAlunoId(alunoId);

        return matriculas.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }
}
