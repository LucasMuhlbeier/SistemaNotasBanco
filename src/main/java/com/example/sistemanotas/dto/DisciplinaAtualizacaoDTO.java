package com.example.sistemanotas.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DisciplinaAtualizacaoDTO {

    @NotNull(message = "O ID da disciplina é obrigatório para atualização")
    private Long id;

    private Long professorId;
    private String descricao;
    private String ementa;

}
