package com.example.sistemanotas.repository;

import com.example.sistemanotas.model.AulasDadas;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AulasDadasRepository extends JpaRepository<AulasDadas, Long> {

    // MÉTODO CRÍTICO: Soluciona o erro de "cannot find symbol"
    // Ele é usado no AulasDadasService para verificar se já existe uma aula na data/disciplina.
    Optional<AulasDadas> findByDisciplinaIdAndData(Long disciplinaId, LocalDate data);

    // Garante a busca e contagem com o tipo Long corrigido
    List<AulasDadas> findByDisciplinaIdOrderByDataDesc(Long disciplinaId);
    Long countByDisciplinaId(Long disciplinaId);
}