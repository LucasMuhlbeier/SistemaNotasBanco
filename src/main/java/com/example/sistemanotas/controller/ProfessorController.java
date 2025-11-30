package com.example.sistemanotas.controller;

import com.example.sistemanotas.dto.ProfessorCadastroDTO;
import com.example.sistemanotas.dto.ProfessorAtualizacaoDTO;
import com.example.sistemanotas.dto.ProfessorResponseDTO;
import com.example.sistemanotas.service.ProfessorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/professor")
@Tag(name = "Professor", description = "Endpoints para gerenciamento de Professores")
public class ProfessorController {

    private final ProfessorService professorService;

    public ProfessorController(ProfessorService professorService) {
        this.professorService = professorService;
    }




    @Operation(summary = "Cadastrar um novo professor", description = "Recebe os dados do professor e verifica a unicidade de CPF/Matrícula.")
    @PostMapping
    public ResponseEntity<ProfessorResponseDTO> cadastrarProfessor(@RequestBody @Valid ProfessorCadastroDTO dto) {
        ProfessorResponseDTO response = professorService.cadastrarProfessor(dto);
        // Retorna 201 Created
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @Operation(summary = "Atualizar dados de um professor", description = "Atualiza o nome do professor pelo ID.")
    @PutMapping
    public ResponseEntity<ProfessorResponseDTO> atualizarProfessor(@RequestBody @Valid ProfessorAtualizacaoDTO dto) {
        ProfessorResponseDTO response = professorService.atualizarProfessor(dto);

        return ResponseEntity.ok(response);
    }


    @Operation(summary = "Buscar professor por ID", description = "Retorna os dados cadastrais de um professor específico.")
    @GetMapping("/{id}")
    public ResponseEntity<ProfessorResponseDTO> buscarProfessorPorId(@PathVariable Long id) {
        ProfessorResponseDTO response = professorService.buscarProfessorPorId(id);

        return ResponseEntity.ok(response);
    }


    @Operation(summary = "Listar todos os professores", description = "Retorna uma lista de todos os professores cadastrados.")
    @GetMapping("/todos")
    public ResponseEntity<List<ProfessorResponseDTO>> listarTodosProfessores() {
        List<ProfessorResponseDTO> response = professorService.listarTodosProfessores();

        return ResponseEntity.ok(response);
    }
}
