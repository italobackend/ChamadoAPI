package com.italobackend.chamadoapi.controller;

import com.italobackend.chamadoapi.dto.request.AuthRequest;
import com.italobackend.chamadoapi.dto.request.UsuarioRequestDTO;
import com.italobackend.chamadoapi.dto.response.AuthResponse;
import com.italobackend.chamadoapi.model.Usuario;
import com.italobackend.chamadoapi.repository.UsuarioRepository;
import com.italobackend.chamadoapi.security.JwtService;
import com.italobackend.chamadoapi.service.UsuarioService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UsuarioService usuarioService;
    private final PasswordEncoder encoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UsuarioRepository usuarioRepository;

    public AuthController(UsuarioService usuarioService, PasswordEncoder encoder, AuthenticationManager authenticationManager, JwtService jwtService, UsuarioRepository usuarioRepository) {
        this.usuarioService = usuarioService;
        this.encoder = encoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.usuarioRepository = usuarioRepository;
    }

    @PostMapping("/registrar")
    public AuthResponse registrar(@RequestBody UsuarioRequestDTO request) {
        Usuario novoUsuario = usuarioService.novoUsuario(request);
        String token = jwtService.gerarToken(novoUsuario);
        return new AuthResponse(token);
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.login(), request.senha()));

        UserDetails userDetails = usuarioRepository.findByLogin(request.login()).orElseThrow();
        String token = jwtService.gerarToken(userDetails);
        return new AuthResponse(token);
    }
}
