package com.tpo.ecommerce.repository;

import com.tpo.ecommerce.entity.Envio;
import com.tpo.ecommerce.enums.EstadoEnvio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface EnvioRepository extends JpaRepository<Envio, Long> {
    
    Optional<Envio> findByOrdenId(Long ordenId);
    Optional<Envio> findByNumSeguimiento(String numSeguimiento);
    List<Envio> findByEstado(EstadoEnvio estado);
    List<Envio> findByTransportista(String transportista);
    
}