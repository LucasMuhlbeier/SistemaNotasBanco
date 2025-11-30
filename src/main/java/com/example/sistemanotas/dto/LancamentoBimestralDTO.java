package com.example.sistemanotas.dto;

import java.math.BigDecimal;


public class LancamentoBimestralDTO {


    private BigDecimal nota;


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
