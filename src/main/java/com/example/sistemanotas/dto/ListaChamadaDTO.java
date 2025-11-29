package com.example.sistemanotas.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.List;

@Data
public class ListaChamadaDTO {

    @NotNull(message = "O ID da aula é obrigatório")
    private Long aulaDadaId;

    @NotEmpty(message = "A lista de alunos e status de falta não pode ser vazia")
    private List<FaltaRegistroDTO> registros;
}