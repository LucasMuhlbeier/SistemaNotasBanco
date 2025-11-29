package com.example.sistemanotas.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AlunoAtualizacaoDTO {

    @NotNull(message = "O ID do aluno é obrigatório para atualização")
    private Long id; // Crucial para o PUT

    // Os outros campos podem ser os mesmos do AlunoCadastroDTO, mas neste caso,
    // vamos permitir a atualização de nome e período, mantendo CPF e RA como únicos (não alteráveis por aqui, idealmente)
    private String nome;
    private Integer periodoAtual;
}