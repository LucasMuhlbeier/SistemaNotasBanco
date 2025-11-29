package com.example.sistemanotas.repository;

import com.example.sistemanotas.model.AlunoDisciplina;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface AlunoDisciplinaRepository extends JpaRepository<AlunoDisciplina, Long> {

    // 1. Busca específica por Aluno e Disciplina (para lançamentos e boletim individual)
    // Usado pelo endpoint: PUT api/aluno/{idAluno}/disciplina/{idDisciplina}/...
    Optional<AlunoDisciplina> findByAlunoIdAndDisciplinaId(Long alunoId, Long disciplinaId);

    // 2. Busca todas as disciplinas que um aluno está cursando (para o Boletim)
    // Usado pelo endpoint: GET api/aluno/{id}/boletim
    List<AlunoDisciplina> findByAlunoId(Long alunoId);

    // 3. Busca todos os alunos matriculados em uma disciplina
    // Usado pelo endpoint: GET api/disciplina/{idDisciplina}/matriculados
    List<AlunoDisciplina> findByDisciplinaId(Long disciplinaId);

    // NOVO MÉTODO (para o método 3): Usado para listar alunos ativos na chamada
    List<AlunoDisciplina> findByDisciplinaIdAndMatriculadoTrue(Long disciplinaId);

    // NOVO MÉTODO (para o método 4): Usado para verificar matrícula ativa no lançamento de falta
    Optional<AlunoDisciplina> findByAlunoIdAndDisciplinaIdAndMatriculadoTrue(Long alunoId, Long disciplinaId);
}