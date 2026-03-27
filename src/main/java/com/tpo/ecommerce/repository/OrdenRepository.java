package com.tpo.ecommerce.repository;

import com.tpo.ecommerce.entity.Orden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrdenRepository extends JpaRepository<Orden, Long> {
    List<Orden> findByCompradorIdOrderByFechaDesc(Long compradorId);
    List<Orden> findDistinctByItemsProductoUsuarioIdOrderByFechaDesc(Long vendedorId);
}
