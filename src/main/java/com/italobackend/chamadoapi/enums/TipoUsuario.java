package com.italobackend.chamadoapi.enums;

public enum TipoUsuario {
    ADMINISTRADOR("Administrador"),
    COMUM("Comum"),
    VISUALIZADOR("Visualizador");

    private final String Descricao;


    TipoUsuario(String descricao) {
        Descricao = descricao;
    }

    public String getDescricao() {
        return Descricao;
    }
}
