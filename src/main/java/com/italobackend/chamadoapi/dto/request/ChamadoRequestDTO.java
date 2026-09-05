package com.italobackend.chamadoapi.dto.request;

import com.italobackend.chamadoapi.enums.TipoChamado;

public record ChamadoRequestDTO(
        String descricao,
        TipoChamado tipoChamado
) {
}
