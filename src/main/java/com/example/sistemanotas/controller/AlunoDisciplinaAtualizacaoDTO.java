package com.example.sistemanotas.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public class AlunoDisciplinaAtualizacaoDTO {


    @NotNull(message = "A nota é obrigatória.")
    @DecimalMin(value = "0.0", inclusive = true, message = "A nota deve ser no mínimo 0.0")
    @DecimalMax(value = "10.0", inclusive = true, message = "A nota deve ser no máximo 10.0")
    private Double nota1Bim;


    @NotNull(message = "O número de faltas é obrigatório.")
    @DecimalMin(value = "0", inclusive = true, message = "O número de faltas não pode ser negativo")
    private Integer faltas1Bim;



    public AlunoDisciplinaAtualizacaoDTO() {
    }

    public AlunoDisciplinaAtualizacaoDTO(Double nota1Bim, Integer faltas1Bim) {
        this.nota1Bim = nota1Bim;
        this.faltas1Bim = faltas1Bim;
    }

    public Double getNota1Bim() {
        return nota1Bim;
    }

    public void setNota1Bim(Double nota1Bim) {
        this.nota1Bim = nota1Bim;
    }

    public Integer getFaltas1Bim() {
        return faltas1Bim;
    }

    public void setFaltas1Bim(Integer faltas1Bim) {
        this.faltas1Bim = faltas1Bim;
    }
}
