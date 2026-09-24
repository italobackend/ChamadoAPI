package com.italobackend.chamadoapi.dto.response;

import com.italobackend.chamadoapi.enums.StatusChamado;
import com.italobackend.chamadoapi.enums.TipoChamado;
import com.italobackend.chamadoapi.model.Chamado;
import com.italobackend.chamadoapi.model.Usuario;

import java.time.LocalDateTime;

public record ChamadoResponseDTO(
        Long id,
        String descricao,
        String usuario,
        TipoChamado tipoChamado,
        StatusChamado status,
        LocalDateTime criadoEm
) {
    public ChamadoResponseDTO(Chamado chamado) {
        this(
                chamado.getId(),
                chamado.getDescricao(),
                chamado.getUsuario().getNome(),
                chamado.getTipoChamado(),
                chamado.getStatus(),
                chamado.getCriadoEm()
        );
    }
}
