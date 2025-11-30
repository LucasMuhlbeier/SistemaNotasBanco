package com.example.sistemanotas.service;

import com.example.sistemanotas.dto.ProfessorCadastroDTO;
import com.example.sistemanotas.dto.ProfessorAtualizacaoDTO;
import com.example.sistemanotas.dto.ProfessorResponseDTO;
import com.example.sistemanotas.model.Professor;
import com.example.sistemanotas.repository.ProfessorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProfessorService {

    private final ProfessorRepository professorRepository;

    public ProfessorService(ProfessorRepository professorRepository) {
        this.professorRepository = professorRepository;
    }


    private Professor toEntity(ProfessorCadastroDTO dto) {
        Professor professor = new Professor();
        professor.setNome(dto.getNome());
        professor.setCpf(dto.getCpf());
        professor.setMatricula(dto.getMatricula());
        return professor;
    }


    public ProfessorResponseDTO toResponseDTO(Professor professor) {
        ProfessorResponseDTO dto = new ProfessorResponseDTO();
        dto.setId(professor.getId());
        dto.setNome(professor.getNome());
        dto.setCpf(professor.getCpf());
        dto.setMatricula(professor.getMatricula());
        return dto;
    }


    @Transactional
    public ProfessorResponseDTO cadastrarProfessor(ProfessorCadastroDTO dto) {

        if (professorRepository.findByCpf(dto.getCpf()).isPresent()) {
            throw new RuntimeException("CPF já cadastrado para outro professor.");
        }
        if (professorRepository.findByMatricula(dto.getMatricula()).isPresent()) {
            throw new RuntimeException("Matrícula já cadastrada.");
        }

        Professor professor = toEntity(dto);
        Professor professorSalvo = professorRepository.save(professor);
        return toResponseDTO(professorSalvo);
    }


    @Transactional
    public ProfessorResponseDTO atualizarProfessor(ProfessorAtualizacaoDTO dto) {
        Professor professor = professorRepository.findById(dto.getId())
                .orElseThrow(() -> new RuntimeException("Professor não encontrado com ID: " + dto.getId()));

        if (dto.getNome() != null) {
            professor.setNome(dto.getNome());
        }


        Professor professorAtualizado = professorRepository.save(professor);
        return toResponseDTO(professorAtualizado);
    }


    public ProfessorResponseDTO buscarProfessorPorId(Long id) {
        Professor professor = professorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Professor não encontrado com ID: " + id));
        return toResponseDTO(professor);
    }


    public Professor buscarEntidadePorId(Long id) {
        return professorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Professor não encontrado com ID: " + id));
    }


    public List<ProfessorResponseDTO> listarTodosProfessores() {
        return professorRepository.findAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }
}
