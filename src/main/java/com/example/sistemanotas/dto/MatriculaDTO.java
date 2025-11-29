package com.example.sistemanotas.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MatriculaDTO {

    @NotNull(message = "O ID do aluno é obrigatório para matrícula")
    private Long alunoId;

    @NotNull(message = "O ID da disciplina é obrigatório para matrícula")
    private Long disciplinaId;
}