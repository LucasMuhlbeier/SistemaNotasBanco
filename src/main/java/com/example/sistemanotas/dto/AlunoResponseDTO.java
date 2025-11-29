package com.example.sistemanotas.dto;

import lombok.Data;

@Data
public class AlunoResponseDTO {

    private Long id;
    private String nome;
    private String cpf;
    private String ra;
    private Integer anoIngresso;
    private Integer periodoAtual;
}