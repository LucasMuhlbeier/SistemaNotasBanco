package com.example.sistemanotas.repository;

import com.example.sistemanotas.model.Disciplina;
import com.example.sistemanotas.model.Professor;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface DisciplinaRepository extends JpaRepository<Disciplina, Long> {

    // Busca uma disciplina pelo código (para o endpoint: GET api/disciplina/{codigo})
    Optional<Disciplina> findByCodigo(String codigo);

    // Lista todas as disciplinas lecionadas por um professor (para o endpoint: GET api/disciplina/{professor})
    List<Disciplina> findByProfessor(Professor professor);

    // Alternativamente, pode-se buscar pela FK (ID do Professor)
    List<Disciplina> findByProfessorId(Long professorId);
}