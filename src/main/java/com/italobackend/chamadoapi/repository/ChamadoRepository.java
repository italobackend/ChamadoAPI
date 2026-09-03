package com.italobackend.chamadoapi.repository;

import com.italobackend.chamadoapi.model.Chamado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChamadoRepository extends JpaRepository<Chamado, Long> {
}
