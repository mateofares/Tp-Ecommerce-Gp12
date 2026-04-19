package com.tpo.ecommerce.repository;

import com.tpo.ecommerce.entity.Direccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DireccionRepository extends JpaRepository<Direccion, Long> {

    List<Direccion> findByUsuarioId(Long usuarioId);

    Optional<Direccion> findByUsuarioIdAndPredeterminada(Long usuarioId, Boolean predeterminada);
    List<Direccion> findAllByUsuarioIdAndPredeterminada(Long usuarioId, Boolean predeterminada);

    List<Direccion> findByUsuarioIdAndActiva(Long usuarioId, Boolean activa);

    Long countByUsuarioId(Long usuarioId);
}

