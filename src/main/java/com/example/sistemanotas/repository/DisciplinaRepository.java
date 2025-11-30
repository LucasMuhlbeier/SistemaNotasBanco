package com.example.sistemanotas.repository;

import com.example.sistemanotas.model.Disciplina;
import com.example.sistemanotas.model.Professor;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface DisciplinaRepository extends JpaRepository<Disciplina, Long> {


    Optional<Disciplina> findByCodigo(String codigo);


    List<Disciplina> findByProfessor(Professor professor);


    List<Disciplina> findByProfessorId(Long professorId);
}
