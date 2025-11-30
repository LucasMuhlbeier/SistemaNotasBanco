package com.example.sistemanotas.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;

@Entity
@Table(name = "aulas_dadas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AulasDadas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne
    @JoinColumn(name = "disciplina_id", nullable = false)
    private Disciplina disciplina;

    @Column(nullable = false)
    private LocalDate data;

    @Column(columnDefinition = "TEXT")
    private String observacoes;


}
