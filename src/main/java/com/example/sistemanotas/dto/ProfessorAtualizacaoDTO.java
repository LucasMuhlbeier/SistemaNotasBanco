package com.example.sistemanotas.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProfessorAtualizacaoDTO {

    @NotNull(message = "O ID do professor é obrigatório para atualização")
    private Long id; // Crucial para identificar quem será atualizado

    private String nome;
    // Matrícula e CPF não são incluídos aqui por segurança, geralmente são imutáveis após o cadastro
}