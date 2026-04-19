package com.tpo.ecommerce.repository;

import com.tpo.ecommerce.entity.Descuento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DescuentoRepository extends JpaRepository<Descuento, Long> {
    Optional<Descuento> findByCodigoDescuento(String codigoDescuento);
}
