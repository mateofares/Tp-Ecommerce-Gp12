package com.tpo.ecommerce.service;

import com.tpo.ecommerce.dto.EnvioDTO;
import com.tpo.ecommerce.enums.EstadoEnvio;
import java.time.LocalDateTime;
import java.util.List;

public interface IEnvioService {
    
    List<EnvioDTO> getAllEnvios();

    EnvioDTO crearEnvio(Long ordenId, String transportista, String numSeguimiento, LocalDateTime fechaEstimada);
    
    EnvioDTO obtenerPorId(Long id);
    
    EnvioDTO obtenerPorOrden(Long ordenId);
    
    EnvioDTO obtenerPorTracking(String numSeguimiento);

    EnvioDTO actualizarEstado(Long id, EstadoEnvio nuevoEstado);

    EnvioDTO registrarEntrega(Long id, LocalDateTime fechaEntrega);

    EnvioDTO generarEnvioAutomatico(Long ordenId);

}