package com.example.sistemanotas.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FaltaRegistroDTO {

    @NotNull(message = "O ID do aluno é obrigatório")
    private Long alunoId;

    @NotNull(message = "O status de falta é obrigatório")
    private Boolean falta; // true = faltou, false = presente
}