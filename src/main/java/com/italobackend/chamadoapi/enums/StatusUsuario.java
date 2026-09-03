package com.italobackend.chamadoapi.enums;

public enum StatusUsuario {
    ATIVO("Ativo"),
    INATIVO("Inativo");

    private final String descricao;

    private StatusUsuario(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
