package com.tpo.ecommerce.repository;

import com.tpo.ecommerce.entity.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PagoRepository extends JpaRepository<Pago, Long> {
    Optional<Pago> findByOrdenId(Long ordenId);
}
