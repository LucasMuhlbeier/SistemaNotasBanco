package com.example.sistemanotas.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "aluno")
@Data // Gera Getters, Setters, toString, equals e hashCode (Lombok)
@NoArgsConstructor // Construtor sem argumentos (JPA e Lombok)
@AllArgsConstructor // Construtor com todos os argumentos (Lombok)
public class Aluno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String cpf;

    @Column(nullable = false, unique = true)
    private String ra; // Registro Acadêmico

    @Column(name = "ano_ingresso")
    private Integer anoIngresso;

    @Column(name = "periodo_atual")
    private Integer periodoAtual;

    // Relacionamento com AlunoDisciplina (para o futuro)
    // Uma vez que implementaremos AlunoDisciplina (matrícula), o relacionamento seria:
    /*
    @OneToMany(mappedBy = "aluno", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AlunoDisciplina> matriculas = new ArrayList<>();
    */
}
