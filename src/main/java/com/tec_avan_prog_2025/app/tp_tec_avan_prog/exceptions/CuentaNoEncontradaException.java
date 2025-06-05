package com.tec_avan_prog_2025.app.tp_tec_avan_prog.exceptions;

public class CuentaNoEncontradaException extends RuntimeException {
    public CuentaNoEncontradaException(String mensaje) {
        super(mensaje);
    }
}