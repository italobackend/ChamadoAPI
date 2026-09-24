package com.italobackend.chamadoapi.controller;

import com.italobackend.chamadoapi.dto.request.ChamadoRequestDTO;
import com.italobackend.chamadoapi.dto.response.ChamadoResponseDTO;
import com.italobackend.chamadoapi.model.Chamado;
import com.italobackend.chamadoapi.service.ChamadoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chamados")
public class ChamadoController {

    private final ChamadoService chamadoService;

    public ChamadoController(ChamadoService chamadoService) {
        this.chamadoService = chamadoService;
    }

    @PostMapping
    public ResponseEntity<ChamadoResponseDTO> novoChamado(@RequestBody ChamadoRequestDTO dto, Authentication authentication) {
        Chamado novoChamado = chamadoService.novoChamado(dto, authentication.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(new ChamadoResponseDTO(novoChamado));
    }

    @GetMapping
    public ResponseEntity<List<ChamadoResponseDTO>> listarChamados() {
        return ResponseEntity.ok().body(chamadoService.listarChamados());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarChamado(@PathVariable Long id) {
        chamadoService.deletarChamado(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Chamado> editarDescricao(@PathVariable Long id, ChamadoRequestDTO dto) {
        Chamado novaDescricao = chamadoService.editarDescricao(id, dto);
        return ResponseEntity.ok().body(novaDescricao);
    }
}
