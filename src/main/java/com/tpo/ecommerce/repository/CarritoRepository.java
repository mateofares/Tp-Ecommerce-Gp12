package com.tpo.ecommerce.repository;

import com.tpo.ecommerce.entity.Carrito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CarritoRepository extends JpaRepository<Carrito, Long> {
    Optional<Carrito> findByCompradorId(Long compradorId);
}
