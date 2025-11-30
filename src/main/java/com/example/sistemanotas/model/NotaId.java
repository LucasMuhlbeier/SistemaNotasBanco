package com.example.sistemanotas.model;

import java.io.Serializable;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotaId implements Serializable {

    private Long alunoId;
    private Long disciplinaId;
    private String bimestre;
}
