package com.example.sistemanotas.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Captura exceções lançadas nos Services e as transforma em respostas HTTP adequadas.
 */
@ControllerAdvice
@RestController
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Trata RuntimeException (usadas nas regras de negócio para indicar erros de lógica ou dados não encontrados).
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handleRuntimeException(RuntimeException ex, WebRequest request) {

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", ex.getMessage());

        // Decisão de Status HTTP baseada no conteúdo da mensagem de erro:
        HttpStatus status = HttpStatus.BAD_REQUEST; // 400 default para erros de negócio/validação

        // Se a mensagem indicar que um recurso não foi encontrado, usamos 404
        if (ex.getMessage().contains("não encontrado") || ex.getMessage().contains("Não encontrada")) {
            status = HttpStatus.NOT_FOUND; // 404
        }

        return new ResponseEntity<>(body, status);
    }

    /**
     * Trata IllegalArgumentException (usadas para argumentos de método inválidos, ex: bimestre != 1 ou 2).
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", ex.getMessage());

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST); // 400
    }

    /**
     * NOVO: Trata erros de validação (@Valid ou @Validated) nos DTOs de entrada.
     * Retorna um 400 Bad Request com a lista de todos os campos com erro.
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatusCode status,
                                                                  WebRequest request) {

        List<String> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.toList());

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Validation Error");
        body.put("message", "A validação do(s) campo(s) falhou.");
        body.put("fields_errors", errors);

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST); // 400
    }
}