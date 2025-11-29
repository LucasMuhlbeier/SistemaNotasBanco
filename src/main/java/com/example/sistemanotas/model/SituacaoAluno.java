package com.example.sistemanotas.model; // Remova o ".enums"

public enum SituacaoAluno {
    APROVADO,
    REPROVADO,
    EM_CURSO,
    EM_EXAME,
    REPROVADO_POR_FALTA, // Adicionado para melhor clareza na lógica
    REPROVADO_POR_NOTA,
    CURSANDO
}