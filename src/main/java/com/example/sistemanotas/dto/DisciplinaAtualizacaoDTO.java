package com.example.sistemanotas.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DisciplinaAtualizacaoDTO {

    @NotNull(message = "O ID da disciplina é obrigatório para atualização")
    private Long id; // ID da Disciplina a ser atualizada

    private Long professorId; // Novo professor (opcional)
    private String descricao;
    private String ementa;
    // O código da disciplina (codigo) geralmente não é alterado
}