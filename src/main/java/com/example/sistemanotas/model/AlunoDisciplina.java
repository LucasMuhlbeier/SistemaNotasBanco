package com.example.sistemanotas.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import com.example.sistemanotas.model.SituacaoAluno;

@Entity
@Table(name = "aluno_disciplina")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlunoDisciplina {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "aluno_id", nullable = false)
    private Aluno aluno;

    @ManyToOne
    @JoinColumn(name = "disciplina_id", nullable = false)
    private Disciplina disciplina;


    @Column(name = "nota_1_bim", precision = 3, scale = 1)
    private BigDecimal nota1Bimestre;

    @Column(name = "nota_2_bim", precision = 3, scale = 1)
    private BigDecimal nota2Bimestre;

    @Column(name = "nota_final", precision = 3, scale = 1)
    private BigDecimal notaFinal;


    @Column(name = "media_parcial", precision = 3, scale = 1)
    private BigDecimal mediaParcial;

    @Column(name = "media_final", precision = 3, scale = 1)
    private BigDecimal mediaFinal;


    @Column(name = "total_aulas_dadas")
    private Long totalAulasDadas = 0L;

    @Column(name = "total_faltas")
    private Long totalFaltas = 0L;

    @Column(name = "percentual_frequencia", precision = 5, scale = 2)
    private BigDecimal percentualFrequencia;

    @Column(nullable = false)
    private Boolean matriculado = true;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SituacaoAluno situacao = SituacaoAluno.CURSANDO;
}
