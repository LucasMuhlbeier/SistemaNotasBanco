package com.example.sistemanotas.dto;

import java.math.BigDecimal;

public class NotaResponseDTO {

    private Long alunoId;
    private Long disciplinaId;
    private String nomeDisciplina;
    private String bimestre;
    private BigDecimal nota;
    private Integer faltas;
    private String situacaoBimestral;


    public NotaResponseDTO() {
    }


    public NotaResponseDTO(Long alunoId, Long disciplinaId, String nomeDisciplina, String bimestre, BigDecimal nota, Integer faltas, String situacaoBimestral) {
        this.alunoId = alunoId;
        this.disciplinaId = disciplinaId;
        this.nomeDisciplina = nomeDisciplina;
        this.bimestre = bimestre;
        this.nota = nota;
        this.faltas = faltas;
        this.situacaoBimestral = situacaoBimestral;
    }



    public Long getAlunoId() {
        return alunoId;
    }

    public void setAlunoId(Long alunoId) {
        this.alunoId = alunoId;
    }

    public Long getDisciplinaId() {
        return disciplinaId;
    }

    public void setDisciplinaId(Long disciplinaId) {
        this.disciplinaId = disciplinaId;
    }

    public String getNomeDisciplina() {
        return nomeDisciplina;
    }

    public void setNomeDisciplina(String nomeDisciplina) {
        this.nomeDisciplina = nomeDisciplina;
    }

    public String getBimestre() {
        return bimestre;
    }

    public void setBimestre(String bimestre) {
        this.bimestre = bimestre;
    }

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

    public String getSituacaoBimestral() {
        return situacaoBimestral;
    }

    public void setSituacaoBimestral(String situacaoBimestral) {
        this.situacaoBimestral = situacaoBimestral;
    }
}
