package com.example.sistemanotas.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class LancamentoNotasDTO {


    @NotNull(message = "O ID da matrícula é obrigatório")
    private Long id;


    @DecimalMin(value = "0.0", message = "A nota deve ser no mínimo 0.0")
    @DecimalMax(value = "10.0", message = "A nota deve ser no máximo 10.0")
    private BigDecimal nota1Bimestre;

    @DecimalMin(value = "0.0", message = "A nota deve ser no mínimo 0.0")
    @DecimalMax(value = "10.0", message = "A nota deve ser no máximo 10.0")
    private BigDecimal nota2Bimestre;

    @DecimalMin(value = "0.0", message = "A nota deve ser no mínimo 0.0")
    @DecimalMax(value = "10.0", message = "A nota deve ser no máximo 10.0")
    private BigDecimal notaFinal;
}
