package com.italobackend.chamadoapi.model;

import com.italobackend.chamadoapi.enums.StatusUsuario;
import com.italobackend.chamadoapi.enums.TipoUsuario;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 256)
    private String nome;

    @Column(nullable = false, length = 50)
    private String login;

    @Column(nullable = false, length = 256)
    private String senha;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_usuario", nullable = false, length = 50)
    private TipoUsuario tipoUsuario;

    @Column(name = "criado_em", nullable = false)
    private LocalDateTime criadoEm;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_usuario", nullable = false, length = 50)
    private StatusUsuario status;

    @OneToMany(mappedBy = "usuario", fetch = FetchType.LAZY)
    private List<Chamado> chamados = new ArrayList<>();

    public Usuario() {
    }

    public Usuario(String nome, String login, String senha, TipoUsuario tipoUsuario, LocalDateTime criadoEm, List<Chamado> chamados, StatusUsuario status) {
        this.nome = nome;
        this.login = login;
        this.senha = senha;
        this.tipoUsuario = tipoUsuario;
        this.criadoEm = LocalDateTime.now();
        this.chamados = chamados;
        this.status = StatusUsuario.ATIVO;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public TipoUsuario getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(TipoUsuario tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    public void setCriadoEm(LocalDateTime criadoEm) {
        this.criadoEm = criadoEm;
    }

    public List<Chamado> getChamados() {
        return chamados;
    }

    public void setChamados(List<Chamado> chamados) {
        this.chamados = chamados;
    }

    public StatusUsuario getStatus() {
        return status;
    }
}
