package com.tpo.ecommerce.repository;

import com.tpo.ecommerce.entity.Resenia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReseniaRepository extends JpaRepository<Resenia, Long> {
    boolean existsByCompradorIdAndProductoId(Long compradorId, Long productoId);
    List<Resenia> findByProductoIdOrderByFechaDesc(Long productoId);
}
