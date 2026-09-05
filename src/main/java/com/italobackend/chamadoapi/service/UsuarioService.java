package com.italobackend.chamadoapi.service;

import com.italobackend.chamadoapi.dto.request.UsuarioRequestDTO;
import com.italobackend.chamadoapi.exceptions.IdNaoEncontradoException;
import com.italobackend.chamadoapi.exceptions.LoginNaoEncontradoException;
import com.italobackend.chamadoapi.exceptions.SenhaIgualException;
import com.italobackend.chamadoapi.exceptions.UsuarioCadastradoException;
import com.italobackend.chamadoapi.model.Usuario;
import com.italobackend.chamadoapi.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Usuario novoUsuario(UsuarioRequestDTO dto) {
        if (usuarioRepository.findByLogin(dto.login()).isPresent()) {
            throw new UsuarioCadastradoException("Login já cadastrado!");
        }
        Usuario usuario = new Usuario(
                dto.nome(),
                dto.login(),
                passwordEncoder.encode(dto.senha())
        );

        return usuarioRepository.save(usuario);
    }

    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    public void deletarUsuario(Long id) {
        usuarioRepository.deleteById(id);
    }

    public Usuario atualizarNome(Long id, UsuarioRequestDTO dto) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new IdNaoEncontradoException("Esse id não foi encontrado!"));
        usuario.setNome(dto.nome());
        return usuarioRepository.save(usuario);
    }

    public void atualizarSenha(Long id, UsuarioRequestDTO dto) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new LoginNaoEncontradoException("Esse id não foi encontrado!"));

        if (usuario.getSenha().equals(dto.senha())) {
            throw new SenhaIgualException("A senha deve ser diferente da senha atual!");
        }
        usuario.setSenha(dto.senha());
        usuarioRepository.save(usuario);
    }
}
