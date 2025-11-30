package com.example.sistemanotas.repository;

import com.example.sistemanotas.model.Nota;
import com.example.sistemanotas.model.NotaId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NotaRepository extends JpaRepository<Nota, NotaId> {

    Optional<Nota> findByAlunoIdAndDisciplinaIdAndBimestre(Long alunoId, Long disciplinaId, String bimestre);
}
