package com.italobackend.chamadoapi.dto.request;

public record AuthRequest (
        String login,
        String senha
){
}
