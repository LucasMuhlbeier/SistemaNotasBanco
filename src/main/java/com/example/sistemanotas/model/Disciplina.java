package com.example.sistemanotas.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "disciplina")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Disciplina {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relacionamento ManyToOne: Muitas Disciplinas para Um Professor (FK)
    @ManyToOne
    @JoinColumn(name = "professor_id", nullable = false) // Nome da coluna da FK
    private Professor professor;

    @Column(nullable = false, unique = true)
    private String codigo; // Ex: ENG001

    @Column(nullable = false)
    private String descricao; // Ex: Engenharia de Software

    @Column(columnDefinition = "TEXT") // Permite textos longos
    private String ementa;

    // Relacionamento com AlunoDisciplina (para o futuro, para listar matrículas)
    /*
    @OneToMany(mappedBy = "disciplina", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AlunoDisciplina> matriculas = new ArrayList<>();
    */
}