package com.italobackend.chamadoapi.dto.request;

import com.italobackend.chamadoapi.enums.StatusChamado;

public record StatusRequestDTO(
        StatusChamado status) {
}
