package com.example.sistemanotas.controller;

import com.example.sistemanotas.dto.AulasDadasCadastroDTO;
import com.example.sistemanotas.dto.AulasDadasResponseDTO;
import com.example.sistemanotas.dto.ListaChamadaDTO;
import com.example.sistemanotas.dto.AlunoResponseDTO;
import com.example.sistemanotas.service.AulasDadasService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/aulas")
@Tag(name = "Controle de Frequência", description = "Endpoints para registro de Aulas e Chamadas")
public class AulasDadasController {

    private final AulasDadasService aulasDadasService;

    public AulasDadasController(AulasDadasService aulasDadasService) {
        this.aulasDadasService = aulasDadasService;
    }

    // 1. Registrar Aula: POST api/aulas
    @Operation(summary = "Registrar uma aula ministrada", description = "Cria um registro para uma aula de uma disciplina em uma data específica.")
    @PostMapping
    public ResponseEntity<AulasDadasResponseDTO> registrarAula(@RequestBody @Valid AulasDadasCadastroDTO dto) {
        AulasDadasResponseDTO response = aulasDadasService.registrarAula(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 2. Lançar Lista de Chamada: POST api/aulas/chamada
    @Operation(summary = "Lançar lista de chamada", description = "Registra a presença/falta de alunos para uma aula. Limpa e recria os registros existentes.")
    @PostMapping("/chamada")
    public ResponseEntity<String> lancarListaChamada(@RequestBody @Valid ListaChamadaDTO dto) {
        String message = aulasDadasService.lancarListaChamada(dto);
        return ResponseEntity.ok(message);
    }

    // 3. Listar Aulas por Disciplina: GET api/aulas/disciplina/{id}
    @Operation(summary = "Listar aulas de uma disciplina", description = "Retorna todas as aulas registradas para uma disciplina.")
    @GetMapping("/disciplina/{disciplinaId}")
    public ResponseEntity<List<AulasDadasResponseDTO>> listarAulasPorDisciplina(@PathVariable Long disciplinaId) {
        List<AulasDadasResponseDTO> response = aulasDadasService.listarAulasPorDisciplina(disciplinaId);
        return ResponseEntity.ok(response);
    }

    // 4. Obter Alunos Matriculados para Chamada: GET api/aulas/{aulaId}/alunos
    @Operation(summary = "Obter lista de alunos para chamada", description = "Retorna todos os alunos matriculados na disciplina referente à aula, pronto para o lançamento da chamada.")
    @GetMapping("/{aulaDadaId}/alunos")
    public ResponseEntity<List<AlunoResponseDTO>> buscarAlunosMatriculados(@PathVariable Long aulaDadaId) {
        List<AlunoResponseDTO> response = aulasDadasService.buscarAlunosMatriculados(aulaDadaId);
        return ResponseEntity.ok(response);
    }
}