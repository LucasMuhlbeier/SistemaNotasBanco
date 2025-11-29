package com.example.sistemanotas.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DisciplinaCadastroDTO {

    @NotNull(message = "O ID do professor é obrigatório")
    private Long professorId; // Recebe apenas o ID da FK

    @NotBlank(message = "O código da disciplina é obrigatório")
    private String codigo; // Ex: ENG001 (deve ser único)

    @NotBlank(message = "A descrição é obrigatória")
    private String descricao; // Ex: Engenharia de Software

    private String ementa;
}