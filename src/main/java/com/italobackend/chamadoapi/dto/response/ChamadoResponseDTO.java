package com.italobackend.chamadoapi.dto.response;

import com.italobackend.chamadoapi.model.Chamado;
import java.time.LocalDateTime;

public record ChamadoResponseDTO(
        Long id,
        String descricao,
        String usuario,
        String tipoChamado,
        String status,
        LocalDateTime criadoEm
) {
    public ChamadoResponseDTO(Chamado chamado) {
        this(
                chamado.getId(),
                chamado.getDescricao(),
                chamado.getUsuario().getNome(),
                chamado.getTipoChamado().getDescricao(),
                chamado.getStatus().getDescricao(),
                chamado.getCriadoEm()
        );
    }
}
