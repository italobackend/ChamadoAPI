package com.italobackend.chamadoapi.enums;

public enum TipoChamado {
    INSTALACAO("Instalação"),
    MANUTENCAO_SOFTWARE("Manutenção de software"),
    MANUTENCAO_DISPOSITIVO("Manutenção de dispositivo"),
    REDE("Manutenção de rede");

    private final String descricao;

    TipoChamado(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
