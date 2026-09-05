package com.italobackend.chamadoapi.dto.request;

public record UsuarioRequestDTO(
        String nome,
        String login,
        String senha
) {
}
