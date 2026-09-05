package com.italobackend.chamadoapi.controller;

import com.italobackend.chamadoapi.dto.request.UsuarioRequestDTO;
import com.italobackend.chamadoapi.model.Usuario;
import com.italobackend.chamadoapi.service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    // Cria novo usuário
    @PostMapping
    public ResponseEntity<Usuario> novoUsuario(@RequestBody UsuarioRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.novoUsuario(dto));
    }

    // Lista os usuários
    @GetMapping
    public ResponseEntity<List<Usuario>> listarUsuarios() {
        return ResponseEntity.status(HttpStatus.OK).body(usuarioService.listarUsuarios());
    }

    // Atualiza apenas o nome
    @PatchMapping("/{id}")
    public ResponseEntity<Usuario> atualizarNome(@PathVariable Long id, @RequestBody UsuarioRequestDTO dto) {
        Usuario usuarioAtualizado = usuarioService.atualizarNome(id, dto);
        return ResponseEntity.ok().body(usuarioAtualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarUsuario(@PathVariable Long id) {
        usuarioService.deletarUsuario(id);
        return ResponseEntity.noContent().build();
    }

}
