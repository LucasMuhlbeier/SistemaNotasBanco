package com.example.sistemanotas.repository;

import com.example.sistemanotas.model.AlunoDisciplina;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface AlunoDisciplinaRepository extends JpaRepository<AlunoDisciplina, Long> {


    Optional<AlunoDisciplina> findByAlunoIdAndDisciplinaId(Long alunoId, Long disciplinaId);


    List<AlunoDisciplina> findByAlunoId(Long alunoId);


    List<AlunoDisciplina> findByDisciplinaId(Long disciplinaId);


    List<AlunoDisciplina> findByDisciplinaIdAndMatriculadoTrue(Long disciplinaId);


    Optional<AlunoDisciplina> findByAlunoIdAndDisciplinaIdAndMatriculadoTrue(Long alunoId, Long disciplinaId);
}
