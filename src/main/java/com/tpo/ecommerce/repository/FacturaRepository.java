package com.tpo.ecommerce.repository;

import com.tpo.ecommerce.entity.Factura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FacturaRepository extends JpaRepository<Factura, Long> {

    Optional<Factura> findByOrdenId(Long ordenId);
    Optional<Factura> findByNumeroFactura(String numeroFactura);

    @Query("SELECT COUNT(f) FROM Factura f WHERE YEAR(f.fechaFactura) = ?1")
    Long countByAnio(int anio);
}

