package com.example.sistemanotas.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProfessorAtualizacaoDTO {

    @NotNull(message = "O ID do professor é obrigatório para atualização")
    private Long id; 
    private String nome;
  
}
