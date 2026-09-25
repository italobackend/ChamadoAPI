package com.italobackend.chamadoapi.service;

import com.italobackend.chamadoapi.dto.request.ChamadoRequestDTO;
import com.italobackend.chamadoapi.dto.request.StatusRequestDTO;
import com.italobackend.chamadoapi.dto.response.ChamadoResponseDTO;
import com.italobackend.chamadoapi.enums.StatusChamado;
import com.italobackend.chamadoapi.exceptions.ChamadoNaoEncontradoException;
import com.italobackend.chamadoapi.model.Chamado;
import com.italobackend.chamadoapi.model.Usuario;
import com.italobackend.chamadoapi.repository.ChamadoRepository;
import com.italobackend.chamadoapi.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class ChamadoService {
    private final ChamadoRepository chamadoRepository;
    private final UsuarioRepository usuarioRepository;


    public ChamadoService(ChamadoRepository chamadoRepository, UsuarioRepository usuarioRepository) {
        this.chamadoRepository = chamadoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public Chamado novoChamado(ChamadoRequestDTO dto, String login) {

        Usuario usuario = usuarioRepository.findByLogin(login)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Chamado novoChamado = new Chamado(dto.descricao(), usuario, dto.tipoChamado(), null);

        return chamadoRepository.save(novoChamado);
    }


    @Transactional(readOnly = true)
    public List<ChamadoResponseDTO> listarChamados() {

        return chamadoRepository.findAll()
                .stream()
                .map(ChamadoResponseDTO::new)
                .toList();
    }

    public void deletarChamado(Long id) {
        chamadoRepository.deleteById(id);
    }

    public Chamado editarDescricao(Long id, ChamadoRequestDTO dto) {
        Chamado chamado = chamadoRepository.findById(id)
                .orElseThrow(() -> new ChamadoNaoEncontradoException("Chamado não encontrado"));

        chamado.setDescricao(dto.descricao());
        return chamadoRepository.save(chamado);
    }

    @Transactional
    public void alterarStatusChamado(Long id, StatusRequestDTO dto) {

        Chamado chamado = chamadoRepository.findById(id)
                .orElseThrow(() -> new ChamadoNaoEncontradoException("Chamado não encontrado"));

        chamado.setStatus(dto.status());
    }

    @Transactional(readOnly = true)
    public Map<String, Long> contarTodosPorStatus() {
        Map<String, Long> contagem = new LinkedHashMap<>();

        for (StatusChamado statusChamado : StatusChamado.values()) {
            contagem.put(statusChamado.getDescricao(), chamadoRepository.countByStatus(statusChamado));
        }

        return contagem;

    }
}
