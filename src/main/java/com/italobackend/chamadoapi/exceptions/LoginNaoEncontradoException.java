package com.italobackend.chamadoapi.exceptions;

public class LoginNaoEncontradoException extends RuntimeException {
    public LoginNaoEncontradoException(String message) {
        super(message);
    }
}
