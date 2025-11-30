package com.example.sistemanotas.dto;

import com.example.sistemanotas.model.SituacaoAluno;
import java.math.BigDecimal;


public class AlunoDisciplinaResponseDTO {

    private Long id;
    private Long alunoId;
    private String alunoNome;
    private Long disciplinaId;
    private String disciplinaCodigo;
    private String disciplinaDescricao;


    private BigDecimal nota1Bimestre;
    private BigDecimal nota2Bimestre;
    private BigDecimal notaFinal;
    private BigDecimal mediaParcial;
    private BigDecimal mediaFinal;


    private Long totalAulasDadas;
    private Long totalFaltas;
    private BigDecimal percentualFrequencia;


    private SituacaoAluno situacao;
    private Boolean matriculado; // CAMPO QUE ESTAVA FALTANDO E CAUSAVA O ERRO



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAlunoId() {
        return alunoId;
    }

    public void setAlunoId(Long alunoId) {
        this.alunoId = alunoId;
    }

    public String getAlunoNome() {
        return alunoNome;
    }

    public void setAlunoNome(String alunoNome) {
        this.alunoNome = alunoNome;
    }

    public Long getDisciplinaId() {
        return disciplinaId;
    }

    public void setDisciplinaId(Long disciplinaId) {
        this.disciplinaId = disciplinaId;
    }

    public String getDisciplinaCodigo() {
        return disciplinaCodigo;
    }

    public void setDisciplinaCodigo(String disciplinaCodigo) {
        this.disciplinaCodigo = disciplinaCodigo;
    }

    public String getDisciplinaDescricao() {
        return disciplinaDescricao;
    }

    public void setDisciplinaDescricao(String disciplinaDescricao) {
        this.disciplinaDescricao = disciplinaDescricao;
    }

    public BigDecimal getNota1Bimestre() {
        return nota1Bimestre;
    }

    public void setNota1Bimestre(BigDecimal nota1Bimestre) {
        this.nota1Bimestre = nota1Bimestre;
    }

    public BigDecimal getNota2Bimestre() {
        return nota2Bimestre;
    }

    public void setNota2Bimestre(BigDecimal nota2Bimestre) {
        this.nota2Bimestre = nota2Bimestre;
    }

    public BigDecimal getNotaFinal() {
        return notaFinal;
    }

    public void setNotaFinal(BigDecimal notaFinal) {
        this.notaFinal = notaFinal;
    }

    public BigDecimal getMediaParcial() {
        return mediaParcial;
    }

    public void setMediaParcial(BigDecimal mediaParcial) {
        this.mediaParcial = mediaParcial;
    }

    public BigDecimal getMediaFinal() {
        return mediaFinal;
    }

    public void setMediaFinal(BigDecimal mediaFinal) {
        this.mediaFinal = mediaFinal;
    }

    public Long getTotalAulasDadas() {
        return totalAulasDadas;
    }

    public void setTotalAulasDadas(Long totalAulasDadas) {
        this.totalAulasDadas = totalAulasDadas;
    }

    public Long getTotalFaltas() {
        return totalFaltas;
    }

    public void setTotalFaltas(Long totalFaltas) {
        this.totalFaltas = totalFaltas;
    }

    public BigDecimal getPercentualFrequencia() {
        return percentualFrequencia;
    }

    public void setPercentualFrequencia(BigDecimal percentualFrequencia) {
        this.percentualFrequencia = percentualFrequencia;
    }

    public SituacaoAluno getSituacao() {
        return situacao;
    }

    public void setSituacao(SituacaoAluno situacao) {
        this.situacao = situacao;
    }

    public Boolean getMatriculado() {
        return matriculado;
    }


    public void setMatriculado(Boolean matriculado) {
        this.matriculado = matriculado;
    }
}
