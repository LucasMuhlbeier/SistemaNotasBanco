package com.example.sistemanotas.controller;

import com.example.sistemanotas.dto.AlunoDisciplinaResponseDTO;
import com.example.sistemanotas.dto.LancamentoNotasDTO;
import com.example.sistemanotas.dto.MatriculaDTO;
import com.example.sistemanotas.service.AlunoDisciplinaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/matricula")
@Tag(name = "Matrícula e Lançamentos", description = "Endpoints para Matrícula, Lançamento de Notas e Consulta de Situação")
public class MatriculaController {

    private final AlunoDisciplinaService alunoDisciplinaService;

    public MatriculaController(AlunoDisciplinaService alunoDisciplinaService) {
        this.alunoDisciplinaService = alunoDisciplinaService;
    }

    // Matricular aluno: POST api/matricula
    @Operation(summary = "Matricular aluno em disciplina", description = "Cria um novo registro AlunoDisciplina.")
    @PostMapping
    public ResponseEntity<AlunoDisciplinaResponseDTO> matricular(@RequestBody @Valid MatriculaDTO dto) {
        AlunoDisciplinaResponseDTO response = alunoDisciplinaService.matricular(dto);
        // Retorna 201 Created
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Lançar/Atualizar Notas: PUT api/matricula/notas
    @Operation(summary = "Lançar ou atualizar notas", description = "Atualiza notas (1° Bimestre, 2° Bimestre, Final) e recalcula a situação do aluno.")
    @PutMapping("/notas")
    public ResponseEntity<AlunoDisciplinaResponseDTO> lancarNotas(@RequestBody @Valid LancamentoNotasDTO dto) {
        AlunoDisciplinaResponseDTO response = alunoDisciplinaService.lancarNotas(dto);
        return ResponseEntity.ok(response);
    }

    // Consultar Matrícula (Boletim por ID de Matrícula): GET api/matricula/{id}
    @Operation(summary = "Consultar status de uma matrícula", description = "Retorna notas, faltas e situação atual da matrícula.")
    @GetMapping("/{id}")
    public ResponseEntity<AlunoDisciplinaResponseDTO> buscarMatriculaPorId(@PathVariable Long id) {
        AlunoDisciplinaResponseDTO response = alunoDisciplinaService.buscarMatriculaPorId(id);
        return ResponseEntity.ok(response);
    }
}