package com.example.sistemanotas.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DisciplinaCadastroDTO {

    @NotNull(message = "O ID do professor é obrigatório")
    private Long professorId;

    @NotBlank(message = "O código da disciplina é obrigatório")
    private String codigo;

    @NotBlank(message = "A descrição é obrigatória")
    private String descricao;

    private String ementa;
}
