package com.example.sistemanotas.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "aulas_dadas_presencas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AulasDadasPresencas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne
    @JoinColumn(name = "aula_dada_id", nullable = false)
    private AulasDadas aulaDada;


    @ManyToOne
    @JoinColumn(name = "aluno_id", nullable = false)
    private Aluno aluno;

    @Column(nullable = false)
    private Boolean falta = false;
}
