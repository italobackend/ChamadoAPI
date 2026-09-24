package com.italobackend.chamadoapi.dto.request;

import com.italobackend.chamadoapi.enums.TipoChamado;
import com.italobackend.chamadoapi.model.Usuario;


public record ChamadoRequestDTO(
        String descricao,
        Usuario usuario,
        TipoChamado tipoChamado
) {
}
