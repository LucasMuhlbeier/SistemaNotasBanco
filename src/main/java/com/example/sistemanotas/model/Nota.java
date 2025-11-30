package com.example.sistemanotas.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "tb_notas")
@IdClass(NotaId.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Nota implements Serializable {


    @Id
    @Column(name = "aluno_id")
    private Long alunoId;

    @Id
    @Column(name = "disciplina_id")
    private Long disciplinaId;

    @Id
    private String bimestre;


    @Column(precision = 4, scale = 2)
    private BigDecimal nota;

    private Integer faltas;


    @Transient
    private String situacaoBimestral;
}
