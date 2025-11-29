package com.example.sistemanotas.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate; // Usamos LocalDate para representar a data

@Entity
@Table(name = "aulas_dadas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AulasDadas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relacionamento ManyToOne: Uma aula pertence a Uma Disciplina (FK)
    @ManyToOne
    @JoinColumn(name = "disciplina_id", nullable = false)
    private Disciplina disciplina;

    @Column(nullable = false)
    private LocalDate data; // Data em que a aula foi ministrada

    @Column(columnDefinition = "TEXT")
    private String observacoes;

    // Relacionamento com AulasDadasPresencas (para o futuro, para listar presenças/faltas)
    // Uma AulaDada terá muitos registros de presença (um para cada aluno matriculado)
    /*
    @OneToMany(mappedBy = "aulaDada", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AulasDadasPresencas> presencas = new ArrayList<>();
    */
}