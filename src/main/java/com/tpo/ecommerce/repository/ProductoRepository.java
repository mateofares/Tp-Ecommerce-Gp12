package com.tpo.ecommerce.repository;

import com.tpo.ecommerce.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    List<Producto> findByUsuarioId(Long usuarioId);
    boolean existsByTitulo(String titulo);
    Optional<Producto> findByTitulo(String titulo);
}
