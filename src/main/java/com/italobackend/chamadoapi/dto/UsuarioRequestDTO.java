package com.italobackend.chamadoapi.dto;

public record UsuarioRequestDTO(
        String nome,
        String login,
        String senha
) {
}
