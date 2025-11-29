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

    // Injeção de dependência do Repository
    public AlunoService(AlunoRepository alunoRepository) {
        this.alunoRepository = alunoRepository;
    }

    // --- MÉTODOS DE CONVERSÃO (MAPPER) ---

    // 1. DTO de Cadastro para Entidade
    private Aluno toEntity(AlunoCadastroDTO dto) {
        Aluno aluno = new Aluno();
        aluno.setNome(dto.getNome());
        aluno.setCpf(dto.getCpf());
        aluno.setRa(dto.getRa());
        aluno.setAnoIngresso(dto.getAnoIngresso());
        aluno.setPeriodoAtual(dto.getPeriodoAtual());
        return aluno;
    }

    // 2. DTO de Resposta (Entidade para DTO)
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

    // --- MÉTODOS DE NEGÓCIO ---

    // Endpoint: POST api/aluno
    @Transactional
    public AlunoResponseDTO cadastrarAluno(AlunoCadastroDTO dto) {
        // Regra de Negócio: Verificar se CPF ou RA já existem
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

    // Endpoint: PUT api/aluno
    @Transactional
    public AlunoResponseDTO atualizarAluno(AlunoAtualizacaoDTO dto) {
        Aluno aluno = alunoRepository.findById(dto.getId())
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado com ID: " + dto.getId()));

        // Atualiza apenas os campos permitidos
        if (dto.getNome() != null) {
            aluno.setNome(dto.getNome());
        }
        if (dto.getPeriodoAtual() != null) {
            aluno.setPeriodoAtual(dto.getPeriodoAtual());
        }

        Aluno alunoAtualizado = alunoRepository.save(aluno);
        return toResponseDTO(alunoAtualizado);
    }

    // Endpoint: GET api/aluno/{id}
    public AlunoResponseDTO buscarAlunoPorId(Long id) {
        Aluno aluno = alunoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado com ID: " + id));
        return toResponseDTO(aluno);
    }

    // Método interno para obter a Entidade (útil para outros Services)
    public Aluno buscarEntidadePorId(Long id) {
        return alunoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado com ID: " + id));
    }

    // Endpoint: GET api/aluno/todos
    public List<AlunoResponseDTO> listarTodosAlunos() {
        return alunoRepository.findAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }
}