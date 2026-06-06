package com.tpo.ecommerce.mapper;

import com.tpo.ecommerce.entity.Envio;
import com.tpo.ecommerce.entity.Orden;
import com.tpo.ecommerce.dto.EnvioDTO;
import org.springframework.stereotype.Component;

@Component
public class MapperEnvio {
    
    public EnvioDTO toDto(Envio envio) {
        if (envio == null) return null;

        EnvioDTO dto = new EnvioDTO(
            envio.getId(),
            envio.getOrden() != null ? envio.getOrden().getId() : null,
            envio.getTransportista(),
            envio.getNumSeguimiento(),
            envio.getFechaEstimada(),
            envio.getEstado(),
            envio.getFechaEntrega()
        );

        if (envio.getOrden() != null) {
            var comprador = envio.getOrden().getComprador();
            if (comprador != null) {
                dto.setCliente(comprador.getNombre() + " " + comprador.getApellido());
            }
            var dir = envio.getOrden().getDireccion();
            if (dir != null) {
                dto.setDireccion(dir.getCalle() + " " + dir.getNumero() + ", " + dir.getCiudad());
            }
        }

        return dto;
    }
    
    
    public Envio toEntity(EnvioDTO dto) {
        if (dto == null) return null;
        
        Envio envio = new Envio();
        envio.setId(dto.getId());
        envio.setTransportista(dto.getTransportista());
        envio.setNumSeguimiento(dto.getNumSeguimiento());
        envio.setFechaEstimada(dto.getFechaEstimada());
        envio.setEstado(dto.getEstado());
        envio.setFechaEntrega(dto.getFechaEntrega());
        
        // Crear STUB de Orden - solo el ID
        if (dto.getOrdenId() != null) {
            Orden orden = new Orden();
            orden.setId(dto.getOrdenId());
            envio.setOrden(orden);
        }
        
        
        return envio;
    }
    
    
    public Envio toEntityConOrden(EnvioDTO dto, Orden orden) {
        if (dto == null) return null;
        
        Envio envio = new Envio();
        envio.setId(dto.getId());
        envio.setTransportista(dto.getTransportista());
        envio.setNumSeguimiento(dto.getNumSeguimiento());
        envio.setFechaEstimada(dto.getFechaEstimada());
        envio.setEstado(dto.getEstado());
        envio.setFechaEntrega(dto.getFechaEntrega());
        envio.setOrden(orden);  // Usar la Orden que ya está cargada
        
        return envio;
    }
    
}