package com.example.sistemanotas.controller;

import com.example.sistemanotas.dto.AlunoCadastroDTO;
import com.example.sistemanotas.dto.AlunoAtualizacaoDTO;
import com.example.sistemanotas.dto.AlunoResponseDTO;
import com.example.sistemanotas.dto.AlunoDisciplinaResponseDTO; // Para o Boletim
import com.example.sistemanotas.service.AlunoService;
import com.example.sistemanotas.service.AlunoDisciplinaService; // Para o Boletim
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Configuração de CORS para permitir acesso do frontend Angular
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/aluno")
@Tag(name = "Aluno", description = "Endpoints para gerenciamento de Alunos e Boletins")
public class AlunoController {

    private final AlunoService alunoService;
    private final AlunoDisciplinaService alunoDisciplinaService; // Serviço de Matrícula/Boletim

    public AlunoController(AlunoService alunoService, AlunoDisciplinaService alunoDisciplinaService) {
        this.alunoService = alunoService;
        this.alunoDisciplinaService = alunoDisciplinaService;
    }

    // --- 1. Endpoints de CRUD de Aluno ---

    // Cadastrar aluno: POST api/aluno
    @Operation(summary = "Cadastrar um novo aluno", description = "Recebe os dados do aluno e verifica a unicidade de CPF/RA.")
    @PostMapping
    public ResponseEntity<AlunoResponseDTO> cadastrarAluno(@RequestBody @Valid AlunoCadastroDTO dto) {
        AlunoResponseDTO response = alunoService.cadastrarAluno(dto);
        // Retorna 201 Created
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Atualizar aluno: PUT api/aluno
    @Operation(summary = "Atualizar dados de um aluno", description = "Atualiza nome e período atual do aluno pelo ID.")
    @PutMapping
    public ResponseEntity<AlunoResponseDTO> atualizarAluno(@RequestBody @Valid AlunoAtualizacaoDTO dto) {
        AlunoResponseDTO response = alunoService.atualizarAluno(dto);
        // Retorna 200 OK
        return ResponseEntity.ok(response);
    }

    // Buscar aluno por id: GET api/aluno/{id}
    @Operation(summary = "Buscar aluno por ID", description = "Retorna os dados cadastrais de um aluno específico.")
    @GetMapping("/{id}")
    public ResponseEntity<AlunoResponseDTO> buscarAlunoPorId(@PathVariable Long id) {
        AlunoResponseDTO response = alunoService.buscarAlunoPorId(id);
        // Retorna 200 OK
        return ResponseEntity.ok(response);
    }

    // Listar todos os alunos: GET api/aluno/todos
    @Operation(summary = "Listar todos os alunos", description = "Retorna uma lista de todos os alunos cadastrados.")
    @GetMapping("/todos")
    public ResponseEntity<List<AlunoResponseDTO>> listarTodosAlunos() {
        List<AlunoResponseDTO> response = alunoService.listarTodosAlunos();
        // Retorna 200 OK
        return ResponseEntity.ok(response);
    }

    // --- 2. Endpoints de Consulta de Boletim ---

    // Listar boletim completo do aluno: GET api/aluno/{alunoId}/boletim
    @Operation(summary = "Gerar boletim completo do aluno", description = "Lista todas as disciplinas matriculadas com notas, faltas e situação final.")
    @GetMapping("/{alunoId}/boletim")
    public ResponseEntity<List<AlunoDisciplinaResponseDTO>> gerarBoletim(@PathVariable Long alunoId) {
        List<AlunoDisciplinaResponseDTO> response = alunoDisciplinaService.listarBoletimPorAluno(alunoId);
        return ResponseEntity.ok(response);
    }
}