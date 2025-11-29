package com.example.sistemanotas.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDate;

@Data
public class AulasDadasCadastroDTO {

    @NotNull(message = "O ID da disciplina é obrigatório")
    private Long disciplinaId;

    @NotNull(message = "A data da aula é obrigatória")
    private LocalDate data;

    private String observacoes;
}