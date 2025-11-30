package com.example.sistemanotas.repository;

import com.example.sistemanotas.model.Aluno;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AlunoRepository extends JpaRepository<Aluno, Long> {

    Optional<Aluno> findByCpf(String cpf);

    Optional<Aluno> findByRa(String ra);
}
