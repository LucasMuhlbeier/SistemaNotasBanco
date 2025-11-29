package com.example.sistemanotas.repository;

import com.example.sistemanotas.model.AulasDadasPresencas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AulasDadasPresencasRepository extends JpaRepository<AulasDadasPresencas, Long> {

    // Método para o serviço verificar se a presença já foi registrada
    Optional<AulasDadasPresencas> findByAulaDadaIdAndAlunoId(Long aulaDadaId, Long alunoId);

    // Método de contagem para o Service de Frequência (CORRIGIDO PARA RETORNAR LONG)
    @Query("SELECT COUNT(adp) FROM AulasDadasPresencas adp WHERE adp.aluno.id = :alunoId AND adp.aulaDada.disciplina.id = :disciplinaId")
    Long countTotalFaltasByAlunoAndDisciplina(@Param("alunoId") Long alunoId, @Param("disciplinaId") Long disciplinaId);

    // Limpeza de registros de uma aula
    void deleteByAulaDadaId(Long aulaDadaId);
}