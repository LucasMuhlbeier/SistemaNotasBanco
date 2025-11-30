package com.example.sistemanotas.service;

import com.example.sistemanotas.dto.NotaAtualizacaoDTO;
import com.example.sistemanotas.dto.NotaResponseDTO;
import com.example.sistemanotas.model.Nota;
import com.example.sistemanotas.repository.NotaRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class NotaService {

    private final NotaRepository notaRepository;

    public NotaService(NotaRepository notaRepository) {
        this.notaRepository = notaRepository;
    }


    public NotaResponseDTO buscarNotaBimestral(Long alunoId, Long disciplinaId, String bimestre) {

        Optional<Nota> notaOpt = notaRepository.findByAlunoIdAndDisciplinaIdAndBimestre(alunoId, disciplinaId, bimestre);

        if (notaOpt.isEmpty()) {
            NotaResponseDTO emptyResponse = new NotaResponseDTO();
            emptyResponse.setAlunoId(alunoId);
            emptyResponse.setDisciplinaId(disciplinaId);
            emptyResponse.setBimestre(bimestre);
            return emptyResponse;
        }

        Nota entity = notaOpt.get();
        return toNotaResponseDTO(entity);
    }

    @Transactional
    public NotaResponseDTO atualizarNotaBimestral(Long alunoId, Long disciplinaId, String bimestre, NotaAtualizacaoDTO dto) {

        Optional<Nota> notaOpt = notaRepository.findByAlunoIdAndDisciplinaIdAndBimestre(alunoId, disciplinaId, bimestre);

        Nota nota = notaOpt.orElseGet(() -> {
            Nota novaNota = new Nota();
            novaNota.setAlunoId(alunoId);
            novaNota.setDisciplinaId(disciplinaId);
            novaNota.setBimestre(bimestre);
            return novaNota;
        });

        nota.setNota(dto.getNota());
        nota.setFaltas(dto.getFaltas());


        System.out.println("DEBUG: Tentando salvar nota: " + nota.getNota() + " Faltas: " + nota.getFaltas() + " para o Bimestre: " + nota.getBimestre());

        Nota savedEntity = notaRepository.save(nota);

        return toNotaResponseDTO(savedEntity);
    }


    private NotaResponseDTO toNotaResponseDTO(Nota entity) {
        NotaResponseDTO dto = new NotaResponseDTO();
        dto.setAlunoId(entity.getAlunoId());
        dto.setDisciplinaId(entity.getDisciplinaId());
        dto.setBimestre(entity.getBimestre());
        dto.setNota(entity.getNota());
        dto.setFaltas(entity.getFaltas());
        dto.setSituacaoBimestral(calcularSituacao(entity));


        System.out.println("DEBUG Mapeamento: Nota no DTO: " + dto.getNota());

        return dto;
    }


    private String calcularSituacao(Nota nota) {
        if (nota.getNota() != null && nota.getNota().doubleValue() >= 7.0) {
            return "Aprovado";
        }
        if (nota.getNota() != null && nota.getNota().doubleValue() >= 4.0) {
            return "Em Recuperação";
        }
        return "Reprovado";
    }

}
