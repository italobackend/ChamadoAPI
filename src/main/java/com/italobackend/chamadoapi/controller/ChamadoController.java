package com.italobackend.chamadoapi.controller;

import com.italobackend.chamadoapi.dto.ChamadoRequestDTO;
import com.italobackend.chamadoapi.model.Chamado;
import com.italobackend.chamadoapi.service.ChamadoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Chamado> novoChamado(@RequestBody ChamadoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<List<Chamado>> listarChamados() {
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
