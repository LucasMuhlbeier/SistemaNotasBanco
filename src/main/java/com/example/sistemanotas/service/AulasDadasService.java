package com.example.sistemanotas.service;

import com.example.sistemanotas.dto.AulasDadasCadastroDTO;
import com.example.sistemanotas.dto.AulasDadasResponseDTO;
import com.example.sistemanotas.dto.FaltaRegistroDTO;
import com.example.sistemanotas.dto.ListaChamadaDTO;
import com.example.sistemanotas.dto.AlunoResponseDTO;
import com.example.sistemanotas.model.AulasDadas;
import com.example.sistemanotas.model.AulasDadasPresencas;
import com.example.sistemanotas.model.Aluno;
import com.example.sistemanotas.model.Disciplina;
import com.example.sistemanotas.repository.AulasDadasRepository;
import com.example.sistemanotas.repository.AulasDadasPresencasRepository;
import com.example.sistemanotas.repository.AlunoDisciplinaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AulasDadasService {

    private final AulasDadasRepository aulasDadasRepository;
    private final AulasDadasPresencasRepository aulasDadasPresencasRepository;
    private final DisciplinaService disciplinaService;
    private final AlunoService alunoService;
    private final AlunoDisciplinaRepository alunoDisciplinaRepository;

    public AulasDadasService(AlunoService alunoService, DisciplinaService disciplinaService,
                             AulasDadasRepository aulasDadasRepository,
                             AulasDadasPresencasRepository aulasDadasPresencasRepository,
                             AlunoDisciplinaRepository alunoDisciplinaRepository) {
        this.aulasDadasRepository = aulasDadasRepository;
        this.aulasDadasPresencasRepository = aulasDadasPresencasRepository;
        this.disciplinaService = disciplinaService;
        this.alunoService = alunoService;
        this.alunoDisciplinaRepository = alunoDisciplinaRepository;
    }

    // --- MÉTODOS DE CONVERSÃO ---

    private AulasDadas toEntity(AulasDadasCadastroDTO dto) {
        AulasDadas aula = new AulasDadas();
        // Garante que a disciplina existe
        Disciplina disciplina = disciplinaService.buscarEntidadePorId(dto.getDisciplinaId());

        aula.setDisciplina(disciplina);
        aula.setData(dto.getData());
        aula.setObservacoes(dto.getObservacoes());
        return aula;
    }

    public AulasDadasResponseDTO toResponseDTO(AulasDadas aula) {
        AulasDadasResponseDTO dto = new AulasDadasResponseDTO();
        dto.setId(aula.getId());
        dto.setData(aula.getData());
        dto.setObservacoes(aula.getObservacoes());

        if (aula.getDisciplina() != null) {
            dto.setDisciplinaId(aula.getDisciplina().getId());
            dto.setDisciplinaCodigo(aula.getDisciplina().getCodigo());
        }
        return dto;
    }

    // --- MÉTODOS DE NEGÓCIO (AULAS) ---

    // 1. Registrar Aula: POST api/aulas
    @Transactional
    public AulasDadasResponseDTO registrarAula(AulasDadasCadastroDTO dto) {
        // Regra: Uma aula por disciplina na mesma data
        if (aulasDadasRepository.findByDisciplinaIdAndData(dto.getDisciplinaId(), dto.getData()).isPresent()) {
            throw new RuntimeException("Já existe uma aula registrada para esta disciplina nesta data.");
        }

        AulasDadas aula = toEntity(dto);
        AulasDadas aulaSalva = aulasDadasRepository.save(aula);
        return toResponseDTO(aulaSalva);
    }

    // 2. Listar Aulas por Disciplina: GET api/aulas/disciplina/{id}
    public List<AulasDadasResponseDTO> listarAulasPorDisciplina(Long disciplinaId) {
        disciplinaService.buscarEntidadePorId(disciplinaId);

        return aulasDadasRepository.findByDisciplinaIdOrderByDataDesc(disciplinaId).stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    // 3. Buscar alunos matriculados para lista de chamada: GET api/aulas/{aulaId}/alunos
    public List<AlunoResponseDTO> buscarAlunosMatriculados(Long aulaDadaId) {
        AulasDadas aula = aulasDadasRepository.findById(aulaDadaId)
                .orElseThrow(() -> new RuntimeException("Aula não encontrada com ID: " + aulaDadaId));

        // Obtém apenas os alunos com matrícula ativa na disciplina desta aula
        List<Aluno> alunos = alunoDisciplinaRepository.findByDisciplinaIdAndMatriculadoTrue(aula.getDisciplina().getId()).stream()
                .map(ad -> ad.getAluno())
                .collect(Collectors.toList());

        return alunos.stream()
                .map(alunoService::toResponseDTO)
                .collect(Collectors.toList());
    }

    // --- MÉTODOS DE NEGÓCIO (PRESENÇA/FALTA) ---

    // 4. Lançar Lista de Chamada: POST api/aulas/chamada
    @Transactional
    public String lancarListaChamada(ListaChamadaDTO dto) {
        AulasDadas aula = aulasDadasRepository.findById(dto.getAulaDadaId())
                .orElseThrow(() -> new RuntimeException("Aula não encontrada com ID: " + dto.getAulaDadaId()));

        // Limpa registros anteriores para permitir a atualização da chamada
        aulasDadasPresencasRepository.deleteByAulaDadaId(aula.getId());

        int faltasRegistradas = 0;

        for (FaltaRegistroDTO registro : dto.getRegistros()) {
            Aluno aluno = alunoService.buscarEntidadePorId(registro.getAlunoId());

            // Regra: Verificar se o aluno está ATUALMENTE matriculado na disciplina desta aula
            if (alunoDisciplinaRepository.findByAlunoIdAndDisciplinaIdAndMatriculadoTrue(
                    aluno.getId(), aula.getDisciplina().getId()).isEmpty()) {

                throw new RuntimeException(
                        "Aluno ID " + aluno.getId() + " não está matriculado ou teve sua matrícula encerrada nesta disciplina."
                );
            }

            // Apenas registra se houver falta (TRUE)
            if (registro.getFalta()) {
                AulasDadasPresencas presenca = new AulasDadasPresencas();
                presenca.setAulaDada(aula);
                presenca.setAluno(aluno);
                presenca.setFalta(true);

                aulasDadasPresencasRepository.save(presenca);
                faltasRegistradas++;
            }
        }

        return String.format("Lista de chamada para a aula ID %d salva com sucesso. %d faltas registradas.", aula.getId(), faltasRegistradas);
    }
}