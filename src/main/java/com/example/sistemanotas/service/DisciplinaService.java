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



    private Disciplina toEntity(DisciplinaCadastroDTO dto) {
        Disciplina disciplina = new Disciplina();


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


        if (disciplina.getProfessor() != null) {
            dto.setProfessorId(disciplina.getProfessor().getId());
            dto.setProfessorNome(disciplina.getProfessor().getNome());
        }
        return dto;
    }




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


    @Transactional
    public DisciplinaResponseDTO atualizarDisciplina(DisciplinaAtualizacaoDTO dto) {
        Disciplina disciplina = disciplinaRepository.findById(dto.getId())
                .orElseThrow(() -> new RuntimeException("Disciplina não encontrada com ID: " + dto.getId()));


        if (dto.getDescricao() != null) {
            disciplina.setDescricao(dto.getDescricao());
        }

        if (dto.getEmenta() != null) {
            disciplina.setEmenta(dto.getEmenta());
        }


        if (dto.getProfessorId() != null) {
            Professor novoProfessor = professorService.buscarEntidadePorId(dto.getProfessorId());
            disciplina.setProfessor(novoProfessor);
        }

        Disciplina disciplinaAtualizada = disciplinaRepository.save(disciplina);
        return toResponseDTO(disciplinaAtualizada);
    }


    public Disciplina buscarEntidadePorId(Long id) {
        return disciplinaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Disciplina não encontrada com ID: " + id));
    }


    public List<DisciplinaResponseDTO> listarTodasDisciplinas() {
        return disciplinaRepository.findAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }


    public DisciplinaResponseDTO buscarDisciplinaPorCodigo(String codigo) {
        Disciplina disciplina = disciplinaRepository.findByCodigo(codigo)
                .orElseThrow(() -> new RuntimeException("Disciplina não encontrada com Código: " + codigo));
        return toResponseDTO(disciplina);
    }


    public List<DisciplinaResponseDTO> listarDisciplinasPorProfessor(Long professorId) {

        List<Disciplina> disciplinas = disciplinaRepository.findByProfessorId(professorId);
        if (disciplinas.isEmpty()) {
            professorService.buscarEntidadePorId(professorId);
        }

        return disciplinas.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }
}
