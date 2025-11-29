package com.example.sistemanotas.controller;

import com.example.sistemanotas.dto.DisciplinaAtualizacaoDTO;
import com.example.sistemanotas.dto.DisciplinaCadastroDTO;
import com.example.sistemanotas.dto.DisciplinaResponseDTO;
import com.example.sistemanotas.service.DisciplinaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/disciplina")
@Tag(name = "Disciplina", description = "Endpoints para gerenciamento de Disciplinas")
public class DisciplinaController {

    private final DisciplinaService disciplinaService;

    public DisciplinaController(DisciplinaService disciplinaService) {
        this.disciplinaService = disciplinaService;
    }

    // Cadastrar disciplina: POST api/disciplina
    @Operation(summary = "Cadastrar nova disciplina", description = "Recebe dados, verifica código único e FK do professor.")
    @PostMapping
    public ResponseEntity<DisciplinaResponseDTO> cadastrarDisciplina(@RequestBody @Valid DisciplinaCadastroDTO dto) {
        DisciplinaResponseDTO response = disciplinaService.cadastrarDisciplina(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Atualizar disciplina: PUT api/disciplina
    @Operation(summary = "Atualizar dados de uma disciplina", description = "Atualiza descrição, ementa ou professor responsável.")
    @PutMapping
    public ResponseEntity<DisciplinaResponseDTO> atualizarDisciplina(@RequestBody @Valid DisciplinaAtualizacaoDTO dto) {
        DisciplinaResponseDTO response = disciplinaService.atualizarDisciplina(dto);
        return ResponseEntity.ok(response);
    }

    // Listar todas as disciplinas: GET api/disciplina/todas
    @Operation(summary = "Listar todas as disciplinas")
    @GetMapping("/todas")
    public ResponseEntity<List<DisciplinaResponseDTO>> listarTodasDisciplinas() {
        List<DisciplinaResponseDTO> response = disciplinaService.listarTodasDisciplinas();
        return ResponseEntity.ok(response);
    }

    // Buscar disciplina por código: GET api/disciplina/{codigo}
    @Operation(summary = "Buscar disciplina por código")
    @GetMapping("/codigo/{codigo}") // Mudança para evitar conflito com {professorId} abaixo
    public ResponseEntity<DisciplinaResponseDTO> buscarDisciplinaPorCodigo(@PathVariable String codigo) {
        DisciplinaResponseDTO response = disciplinaService.buscarDisciplinaPorCodigo(codigo);
        return ResponseEntity.ok(response);
    }

    // Listar disciplinas de um professor: GET api/disciplina/professor/{professorId}
    // Mudança para evitar ambiguidade com {codigo}
    @Operation(summary = "Listar disciplinas de um professor", description = "Lista todas as disciplinas lecionadas pelo professor com o ID fornecido.")
    @GetMapping("/professor/{professorId}")
    public ResponseEntity<List<DisciplinaResponseDTO>> listarDisciplinasPorProfessor(@PathVariable Long professorId) {
        List<DisciplinaResponseDTO> response = disciplinaService.listarDisciplinasPorProfessor(professorId);
        return ResponseEntity.ok(response);
    }

    // Listar alunos matriculados em uma disciplina: GET api/disciplina/{idDisciplina}/matriculados
    // Este endpoint será implementado após a criação do AlunoDisciplinaService.
    /*
    @GetMapping("/{idDisciplina}/matriculados")
    public ResponseEntity<List<AlunoResponseDTO>> listarAlunosMatriculados(@PathVariable Long idDisciplina) {
        // Lógica futura
    }
    */
}