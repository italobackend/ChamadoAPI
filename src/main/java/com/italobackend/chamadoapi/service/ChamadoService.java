package com.italobackend.chamadoapi.service;

import com.italobackend.chamadoapi.dto.request.ChamadoRequestDTO;
import com.italobackend.chamadoapi.exceptions.ChamadoNaoEncontradoException;
import com.italobackend.chamadoapi.model.Chamado;
import com.italobackend.chamadoapi.repository.ChamadoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChamadoService {
    private final ChamadoRepository chamadoRepository;

    public ChamadoService(ChamadoRepository chamadoRepository) {
        this.chamadoRepository = chamadoRepository;
    }

    public Chamado novoChamado(ChamadoRequestDTO dto) {

        Chamado novoChamado = new Chamado();
        novoChamado.setDescricao(dto.descricao());
        novoChamado.setTipoChamado(dto.tipoChamado());

        return chamadoRepository.save(novoChamado);
    }

    public List<Chamado> listarChamados() {
        return chamadoRepository.findAll();
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
}
