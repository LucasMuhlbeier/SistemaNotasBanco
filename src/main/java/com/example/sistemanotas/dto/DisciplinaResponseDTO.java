package com.example.sistemanotas.dto;

import lombok.Data;

@Data
public class DisciplinaResponseDTO {

    private Long id;
    private String codigo;
    private String descricao;
    private String ementa;
    private Long professorId;
    private String professorNome; 
}
