package com.italobackend.chamadoapi.model;

import com.italobackend.chamadoapi.enums.TipoChamado;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "chamados")
public class Chamado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 256)
    private String descricao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_chamado", nullable = false, length = 100)
    private TipoChamado tipoChamado;

    @Column(name = "criado_em", nullable = false)
    private LocalDateTime criadoEm;

    public Chamado() {
    }

    public Chamado(String descricao, Usuario usuario, TipoChamado tipoChamado, LocalDateTime criadoEm) {
        this.descricao = descricao;
        this.usuario = usuario;
        this.tipoChamado = tipoChamado;
        this.criadoEm = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public TipoChamado getTipoChamado() {
        return tipoChamado;
    }

    public void setTipoChamado(TipoChamado tipoChamado) {
        this.tipoChamado = tipoChamado;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    public void setCriadoEm(LocalDateTime criadoEm) {
        this.criadoEm = criadoEm;
    }
}
