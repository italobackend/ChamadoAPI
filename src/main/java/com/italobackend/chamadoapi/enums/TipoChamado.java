package com.italobackend.chamadoapi.enums;

public enum TipoChamado {
    INSTALACAO("Instalação"),
    MANUTENCAO_SOFTWARE("Software"),
    MANUTENCAO_DISPOSITIVO("Dispositivo"),
    REDE("Rede");

    private final String descricao;

    TipoChamado(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
