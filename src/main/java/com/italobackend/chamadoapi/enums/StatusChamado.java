package com.italobackend.chamadoapi.enums;

public enum StatusChamado {
    ABERTO("Em aberto"),
    ANDAMENTO("Em andamento"),
    CONCLUIDO("Concluído"),
    ARQUIVADO("Arquivado");

    private final String descricao;

    StatusChamado(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
