package com.example.sistemanotas.service;

import com.example.sistemanotas.dto.AlunoCadastroDTO;
import com.example.sistemanotas.dto.AlunoAtualizacaoDTO;
import com.example.sistemanotas.dto.AlunoResponseDTO;
import com.example.sistemanotas.model.Aluno;
import com.example.sistemanotas.repository.AlunoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AlunoService {

    private final AlunoRepository alunoRepository;


    public AlunoService(AlunoRepository alunoRepository) {
        this.alunoRepository = alunoRepository;
    }



    private Aluno toEntity(AlunoCadastroDTO dto) {
        Aluno aluno = new Aluno();
        aluno.setNome(dto.getNome());
        aluno.setCpf(dto.getCpf());
        aluno.setRa(dto.getRa());
        aluno.setAnoIngresso(dto.getAnoIngresso());
        aluno.setPeriodoAtual(dto.getPeriodoAtual());
        return aluno;
    }


    public AlunoResponseDTO toResponseDTO(Aluno aluno) {
        AlunoResponseDTO dto = new AlunoResponseDTO();
        dto.setId(aluno.getId());
        dto.setNome(aluno.getNome());
        dto.setCpf(aluno.getCpf());
        dto.setRa(aluno.getRa());
        dto.setAnoIngresso(aluno.getAnoIngresso());
        dto.setPeriodoAtual(aluno.getPeriodoAtual());
        return dto;
    }



    @Transactional
    public AlunoResponseDTO cadastrarAluno(AlunoCadastroDTO dto) {

        if (alunoRepository.findByCpf(dto.getCpf()).isPresent()) {
            throw new RuntimeException("CPF já cadastrado.");
        }
        if (alunoRepository.findByRa(dto.getRa()).isPresent()) {
            throw new RuntimeException("RA já cadastrado.");
        }

        Aluno aluno = toEntity(dto);
        Aluno alunoSalvo = alunoRepository.save(aluno);
        return toResponseDTO(alunoSalvo);
    }


    @Transactional
    public AlunoResponseDTO atualizarAluno(AlunoAtualizacaoDTO dto) {
        Aluno aluno = alunoRepository.findById(dto.getId())
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado com ID: " + dto.getId()));


        if (dto.getNome() != null) {
            aluno.setNome(dto.getNome());
        }
        if (dto.getPeriodoAtual() != null) {
            aluno.setPeriodoAtual(dto.getPeriodoAtual());
        }

        Aluno alunoAtualizado = alunoRepository.save(aluno);
        return toResponseDTO(alunoAtualizado);
    }


    public AlunoResponseDTO buscarAlunoPorId(Long id) {
        Aluno aluno = alunoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado com ID: " + id));
        return toResponseDTO(aluno);
    }


    public Aluno buscarEntidadePorId(Long id) {
        return alunoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado com ID: " + id));
    }


    public List<AlunoResponseDTO> listarTodosAlunos() {
        return alunoRepository.findAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }
}
