package com.example.sistemanotas.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class AulasDadasResponseDTO {

    private Long id;
    private Long disciplinaId;
    private String disciplinaCodigo;
    private LocalDate data;
    private String observacoes;
}