package com.italobackend.chamadoapi.exceptions;

public class ChamadoNaoEncontradoException extends RuntimeException {
    public ChamadoNaoEncontradoException(String message) {
        super(message);
    }
}
