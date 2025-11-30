package com.example.sistemanotas.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ProfessorCadastroDTO {

    @NotBlank(message = "O número de matrícula é obrigatório")
    private String matricula; 

    @NotBlank(message = "O nome é obrigatório")
    private String nome;

    @NotBlank(message = "O CPF é obrigatório")
    private String cpf; 
}
