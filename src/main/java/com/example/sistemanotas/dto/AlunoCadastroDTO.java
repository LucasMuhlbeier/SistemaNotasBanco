package com.example.sistemanotas.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AlunoCadastroDTO {

    @NotBlank(message = "O nome é obrigatório")
    private String nome;

    @NotBlank(message = "O CPF é obrigatório")
    private String cpf;

    @NotBlank(message = "O RA é obrigatório")
    private String ra;

    @NotNull(message = "O ano de ingresso é obrigatório")
    private Integer anoIngresso;

    @NotNull(message = "O período atual é obrigatório")
    private Integer periodoAtual;
}