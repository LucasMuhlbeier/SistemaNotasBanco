package com.example.sistemanotas.repository; 

import com.example.sistemanotas.model.Professor; 
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ProfessorRepository extends JpaRepository<Professor, Long> {

    Optional<Professor> findByMatricula(String matricula);

    Optional<Professor> findByCpf(String cpf);
}
