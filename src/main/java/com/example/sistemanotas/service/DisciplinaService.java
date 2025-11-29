package com.example.sistemanotas.service;

import com.example.sistemanotas.dto.DisciplinaCadastroDTO;
import com.example.sistemanotas.dto.DisciplinaAtualizacaoDTO;
import com.example.sistemanotas.dto.DisciplinaResponseDTO;
import com.example.sistemanotas.model.Disciplina;
import com.example.sistemanotas.model.Professor;
import com.example.sistemanotas.repository.DisciplinaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DisciplinaService {

    private final DisciplinaRepository disciplinaRepository;
    private final ProfessorService professorService; // Injetando o ProfessorService

    public DisciplinaService(DisciplinaRepository disciplinaRepository, ProfessorService professorService) {
        this.disciplinaRepository = disciplinaRepository;
        this.professorService = professorService;
    }

    // --- MÉTODOS DE CONVERSÃO (MAPPER) ---

    private Disciplina toEntity(DisciplinaCadastroDTO dto) {
        Disciplina disciplina = new Disciplina();

        // 1. Buscar a Entidade Professor pela FK
        Professor professor = professorService.buscarEntidadePorId(dto.getProfessorId());
        disciplina.setProfessor(professor);

        disciplina.setCodigo(dto.getCodigo());
        disciplina.setDescricao(dto.getDescricao());
        disciplina.setEmenta(dto.getEmenta());
        return disciplina;
    }

    public DisciplinaResponseDTO toResponseDTO(Disciplina disciplina) {
        DisciplinaResponseDTO dto = new DisciplinaResponseDTO();
        dto.setId(disciplina.getId());
        dto.setCodigo(disciplina.getCodigo());
        dto.setDescricao(disciplina.getDescricao());
        dto.setEmenta(disciplina.getEmenta());

        // 2. Mapeamento da Chave Estrangeira (Professor)
        if (disciplina.getProfessor() != null) {
            dto.setProfessorId(disciplina.getProfessor().getId());
            dto.setProfessorNome(disciplina.getProfessor().getNome());
        }
        return dto;
    }

    // --- MÉTODOS DE NEGÓCIO ---

    // Endpoint: POST api/disciplina
    @Transactional
    public DisciplinaResponseDTO cadastrarDisciplina(DisciplinaCadastroDTO dto) {
        // Regra de Negócio: Verificar se o código já existe
        if (disciplinaRepository.findByCodigo(dto.getCodigo()).isPresent()) {
            throw new RuntimeException("Código de disciplina já cadastrado: " + dto.getCodigo());
        }

        Disciplina disciplina = toEntity(dto);
        Disciplina disciplinaSalva = disciplinaRepository.save(disciplina);
        return toResponseDTO(disciplinaSalva);
    }

    // Endpoint: PUT api/disciplina
    @Transactional
    public DisciplinaResponseDTO atualizarDisciplina(DisciplinaAtualizacaoDTO dto) {
        Disciplina disciplina = disciplinaRepository.findById(dto.getId())
                .orElseThrow(() -> new RuntimeException("Disciplina não encontrada com ID: " + dto.getId()));

        // Atualiza a descrição
        if (dto.getDescricao() != null) {
            disciplina.setDescricao(dto.getDescricao());
        }
        // Atualiza a ementa
        if (dto.getEmenta() != null) {
            disciplina.setEmenta(dto.getEmenta());
        }

        // Atualiza o professor (se um novo ID for fornecido)
        if (dto.getProfessorId() != null) {
            Professor novoProfessor = professorService.buscarEntidadePorId(dto.getProfessorId());
            disciplina.setProfessor(novoProfessor);
        }

        Disciplina disciplinaAtualizada = disciplinaRepository.save(disciplina);
        return toResponseDTO(disciplinaAtualizada);
    }

    // Método interno para obter a Entidade (útil para AlunoDisciplinaService)
    public Disciplina buscarEntidadePorId(Long id) {
        return disciplinaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Disciplina não encontrada com ID: " + id));
    }

    // Endpoint: GET api/disciplina/todas
    public List<DisciplinaResponseDTO> listarTodasDisciplinas() {
        return disciplinaRepository.findAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    // Endpoint: GET api/disciplina/{codigo}
    public DisciplinaResponseDTO buscarDisciplinaPorCodigo(String codigo) {
        Disciplina disciplina = disciplinaRepository.findByCodigo(codigo)
                .orElseThrow(() -> new RuntimeException("Disciplina não encontrada com Código: " + codigo));
        return toResponseDTO(disciplina);
    }

    // Endpoint: GET api/disciplina/{professorId}
    public List<DisciplinaResponseDTO> listarDisciplinasPorProfessor(Long professorId) {
        // Usa o método customizado do DisciplinaRepository (findByProfessorId)
        List<Disciplina> disciplinas = disciplinaRepository.findByProfessorId(professorId);
        if (disciplinas.isEmpty()) {
            professorService.buscarEntidadePorId(professorId); // Lança erro se o professor não existir
        }

        return disciplinas.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }
}