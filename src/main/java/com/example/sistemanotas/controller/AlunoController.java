package com.example.sistemanotas.controller;

import com.example.sistemanotas.dto.*;
import com.example.sistemanotas.service.AlunoService;
import com.example.sistemanotas.service.AlunoDisciplinaService;
import com.example.sistemanotas.service.NotaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/aluno")
@Tag(name = "Aluno", description = "Endpoints para gerenciamento de Alunos e Boletins")
public class AlunoController {

    private final AlunoService alunoService;
    private final AlunoDisciplinaService alunoDisciplinaService;
    private final NotaService notaService;

    public AlunoController(AlunoService alunoService, AlunoDisciplinaService alunoDisciplinaService, NotaService notaService) {
        this.alunoService = alunoService;
        this.alunoDisciplinaService = alunoDisciplinaService;
        this.notaService = notaService;
    }



    @Operation(summary = "Cadastrar um novo aluno", description = "Recebe os dados do aluno e verifica a unicidade de CPF/RA.")
    @PostMapping
    public ResponseEntity<AlunoResponseDTO> cadastrarAluno(@RequestBody @Valid AlunoCadastroDTO dto) {
        AlunoResponseDTO response = alunoService.cadastrarAluno(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @Operation(summary = "Atualizar dados de um aluno", description = "Atualiza nome e período atual do aluno pelo ID.")
    @PutMapping
    public ResponseEntity<AlunoResponseDTO> atualizarAluno(@RequestBody @Valid AlunoAtualizacaoDTO dto) {
        AlunoResponseDTO response = alunoService.atualizarAluno(dto);

        return ResponseEntity.ok(response);
    }


    @Operation(summary = "Buscar aluno por ID", description = "Retorna os dados cadastrais de um aluno específico.")
    @GetMapping("/{id}")
    public ResponseEntity<AlunoResponseDTO> buscarAlunoPorId(@PathVariable Long id) {
        AlunoResponseDTO response = alunoService.buscarAlunoPorId(id);

        return ResponseEntity.ok(response);
    }


    @Operation(summary = "Listar todos os alunos", description = "Retorna uma lista de todos os alunos cadastrados.")
    @GetMapping("/todos")
    public ResponseEntity<List<AlunoResponseDTO>> listarTodosAlunos() {
        List<AlunoResponseDTO> response = alunoService.listarTodosAlunos();

        return ResponseEntity.ok(response);
    }


    @Operation(summary = "Gerar boletim completo do aluno", description = "Lista todas as disciplinas matriculadas com notas, faltas e situação final.")
    @GetMapping("/{alunoId}/boletim")
    public ResponseEntity<List<AlunoDisciplinaResponseDTO>> gerarBoletim(@PathVariable Long alunoId) {
        List<AlunoDisciplinaResponseDTO> response = alunoDisciplinaService.listarBoletimPorAluno(alunoId);
        return ResponseEntity.ok(response);
    }


    @Operation(summary = "Buscar nota bimestral", description = "Retorna a nota do aluno em uma disciplina para o 1º ou 2º bimestre.")
    @GetMapping("/{alunoId}/disciplina/{disciplinaId}/{bimestre}")
    public ResponseEntity<NotaResponseDTO> buscarNotaBimestral(
            @PathVariable Long alunoId,
            @PathVariable Long disciplinaId,
            @PathVariable String bimestre) {


        if (!"1bim".equalsIgnoreCase(bimestre) && !"2bim".equalsIgnoreCase(bimestre)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }


        NotaResponseDTO response = notaService.buscarNotaBimestral(alunoId, disciplinaId, bimestre);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Cadastrar/Atualizar Nota Bimestral (PUT)", description = "Cadastra se não existir, ou atualiza a nota e as faltas de um aluno em uma disciplina e bimestre.")
    @PutMapping("/{alunoId}/disciplina/{disciplinaId}/{bimestre}")
    public ResponseEntity<NotaResponseDTO> atualizarNotaBimestral(
            @PathVariable Long alunoId,
            @PathVariable Long disciplinaId,
            @PathVariable String bimestre,
            @RequestBody @Valid NotaAtualizacaoDTO dto) {


        if (!"1bim".equalsIgnoreCase(bimestre) && !"2bim".equalsIgnoreCase(bimestre)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }


        NotaResponseDTO response = notaService.atualizarNotaBimestral(alunoId, disciplinaId, bimestre, dto);


        return ResponseEntity.ok(response);
    }
}
