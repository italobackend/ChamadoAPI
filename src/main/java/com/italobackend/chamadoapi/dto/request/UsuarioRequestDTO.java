package com.italobackend.chamadoapi.dto.request;

import com.italobackend.chamadoapi.model.Usuario;

public record UsuarioRequestDTO(
        String nome,
        String login,
        String senha
) {
}
