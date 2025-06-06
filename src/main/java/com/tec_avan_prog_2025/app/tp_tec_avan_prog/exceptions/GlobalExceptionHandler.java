package com.tec_avan_prog_2025.app.tp_tec_avan_prog.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private ResponseEntity<Object> buildResponse(HttpStatus status, String mensaje) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("error", mensaje);
        return new ResponseEntity<>(body, status);
    }

    // Errores de tipo entidad no encontrada
    @ExceptionHandler({
        CuentaNoEncontradaException.class,
        EntradaNoEncontradaException.class,
        FuncionNoEncontradaException.class,
        ArtistaNoEncontradoException.class,
        SalaNoEncontradaException.class
    })
    public ResponseEntity<Object> handleNotFound(RuntimeException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    // Errores de validación
    @ExceptionHandler({
        TipoEntradaInvalidoException.class,
        FuncionPasadaException.class,
        CapacidadExcedidaException.class,
        SuperposicionFuncionException.class,
        AccesoDenegadoException.class
    })
    public ResponseEntity<Object> handleBadRequest(RuntimeException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    // Error inesperado
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGeneralError(Exception ex) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Ocurrió un error inesperado.");
    }
}
