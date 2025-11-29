package com.example.sistemanotas.dto;

import java.math.BigDecimal;

/**
 * DTO para lançamento de nota e faltas em um bimestre específico.
 */
public class LancamentoBimestralDTO {

    // Nota do bimestre (1 ou 2)
    private BigDecimal nota;

    // Faltas registradas no bimestre (este campo é mantido para aderir ao enunciado,
    // mas o cálculo de faltas será sempre feito pelo total em AulasDadasPresencas).
    private Integer faltas;

    // Construtores, Getters e Setters

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