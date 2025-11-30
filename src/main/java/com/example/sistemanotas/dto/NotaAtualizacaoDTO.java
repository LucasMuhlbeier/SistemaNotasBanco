package com.example.sistemanotas.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public class NotaAtualizacaoDTO {


    @NotNull(message = "A nota é obrigatória.")
    @DecimalMin(value = "0.0", inclusive = true, message = "A nota não pode ser negativa.")
    private BigDecimal nota;


    @NotNull(message = "O número de faltas é obrigatório.")
    @Min(value = 0, message = "O número de faltas não pode ser negativo.")
    @Max(value = 100, message = "O número máximo de faltas permitido foi excedido.")
    private Integer faltas;



    public BigDecimal getNota() {
        return nota;
    }

    public void setNota(BigDecimal nota) {
        this.nota = nota;
    }

    public Integer getFaltas() {
        return faltas;
    }

    public void setFaltas(Integer faltas) {
        this.faltas = faltas;
    }
}
