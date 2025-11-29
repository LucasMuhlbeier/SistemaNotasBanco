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

    // Relacionamento ManyToOne: Muitas presenças para Uma Aula Dada (FK)
    @ManyToOne
    @JoinColumn(name = "aula_dada_id", nullable = false)
    private AulasDadas aulaDada;

    // Relacionamento ManyToOne: Muitas presenças para Um Aluno (FK)
    @ManyToOne
    @JoinColumn(name = "aluno_id", nullable = false)
    private Aluno aluno;

    @Column(nullable = false)
    private Boolean falta = false; // true se o aluno faltou, false se presente
}