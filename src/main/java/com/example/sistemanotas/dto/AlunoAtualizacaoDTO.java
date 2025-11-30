package com.example.sistemanotas.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AlunoAtualizacaoDTO {

    @NotNull(message = "O ID do aluno é obrigatório para atualização")
    private Long id;


    private String nome;
    private Integer periodoAtual;
}
