package com.example.sistemanotas.repository;

import com.example.sistemanotas.model.AulasDadas;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AulasDadasRepository extends JpaRepository<AulasDadas, Long> {


    Optional<AulasDadas> findByDisciplinaIdAndData(Long disciplinaId, LocalDate data);

   
    List<AulasDadas> findByDisciplinaIdOrderByDataDesc(Long disciplinaId);
    Long countByDisciplinaId(Long disciplinaId);
}
