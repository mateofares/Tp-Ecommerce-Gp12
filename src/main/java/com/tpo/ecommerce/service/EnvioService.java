package com.tpo.ecommerce.service;

import com.tpo.ecommerce.entity.Envio;
import com.tpo.ecommerce.entity.Orden;
import com.tpo.ecommerce.enums.EstadoEnvio;
import com.tpo.ecommerce.enums.EstadoOrden;
import com.tpo.ecommerce.dto.EnvioDTO;
import com.tpo.ecommerce.repository.EnvioRepository;
import com.tpo.ecommerce.repository.OrdenRepository;
import com.tpo.ecommerce.mapper.MapperEnvio;
import com.tpo.ecommerce.exceptions.NotFoundException;
import com.tpo.ecommerce.exceptions.BadRequestException;
import org.springframework.stereotype.Service;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class EnvioService implements IEnvioService {
    
    private final EnvioRepository envioRepository;
    private final OrdenRepository ordenRepository;
    private final MapperEnvio mapperEnvio;
    
    @Override
    public EnvioDTO crearEnvio(Long ordenId, String transportista, String numSeguimiento,
                               LocalDateTime fechaEstimada) {
        
        
        Orden orden = ordenRepository.findById(ordenId)
            .orElseThrow(() -> new NotFoundException("Orden no encontrada con id: " + ordenId));
        
        
        if (orden.getEstado()!=EstadoOrden.PAGADA) {
            throw new BadRequestException("La orden debe estar pagada para registrar envío. Estado actual: " + orden.getEstado());
        }
        
        
        if (envioRepository.findByOrdenId(ordenId).isPresent()) {
            throw new BadRequestException("La orden " + ordenId + " ya tiene un envío registrado");
        }
        
        if (envioRepository.findByNumSeguimiento(numSeguimiento).isPresent()) {
            throw new BadRequestException("El número de seguimiento " + numSeguimiento + " ya existe");
        }
        
        Envio envio = new Envio(orden, transportista, numSeguimiento, fechaEstimada);
        
        
        Envio envioGuardado = envioRepository.save(envio);
        
        
        return mapperEnvio.toDto(envioGuardado);
    }
    
    @Override
    public EnvioDTO obtenerPorId(Long id) {
        Envio envio = envioRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Envío no encontrado con id: " + id));
        return mapperEnvio.toDto(envio);
    }
    
    @Override
    public EnvioDTO obtenerPorOrden(Long ordenId) {
        
        if (!ordenRepository.existsById(ordenId)) {
            throw new NotFoundException("Orden no encontrada con id: " + ordenId);
        }
        
        
        Envio envio = envioRepository.findByOrdenId(ordenId)
            .orElseThrow(() -> new NotFoundException("No existe envío para la orden: " + ordenId));
        
        return mapperEnvio.toDto(envio);
    }
    
    @Override
    public EnvioDTO obtenerPorTracking(String numSeguimiento) {
        
        Envio envio = envioRepository.findByNumSeguimiento(numSeguimiento)
            .orElseThrow(() -> new NotFoundException("Número de seguimiento no encontrado: " + numSeguimiento));
        
        return mapperEnvio.toDto(envio);
        // El Controller filtra qué datos expone (no mostrar ordenId privado)
    }
    
    @Override
    public EnvioDTO actualizarEstado(Long id, EstadoEnvio nuevoEstado) {
        
        
        Envio envio = envioRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Envío no encontrado con id: " + id));
        
        
        EstadoEnvio estadoActual = envio.getEstado();
        
        if (estadoActual.equals(EstadoEnvio.ENTREGADO)) {
            throw new BadRequestException("No se puede cambiar el estado de un envío ENTREGADO");
        }
        if (estadoActual.equals(EstadoEnvio.CANCELADO)) {
            throw new BadRequestException("No se puede cambiar el estado de un envío CANCELADO");
        }
        
        envio.setEstado(nuevoEstado);
        
        
        Envio envioActualizado = envioRepository.save(envio);
        
        return mapperEnvio.toDto(envioActualizado);
    }
    
    @Override
    public EnvioDTO registrarEntrega(Long id, LocalDateTime fechaEntrega) {
        
        
        Envio envio = envioRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Envío no encontrado con id: " + id));
        
        
        if (!envio.getEstado().equals(EstadoEnvio.EN_TRANSITO)) {
            throw new BadRequestException("Solo se puede entregar un envío EN_TRANSITO. Estado: " + envio.getEstado());
        }
        
        if (fechaEntrega.isAfter(LocalDateTime.now())) {
            throw new BadRequestException("La fecha de entrega no puede ser futura");
        }
        
        
        envio.setEstado(EstadoEnvio.ENTREGADO);
        envio.setFechaEntrega(fechaEntrega);
        
        Envio envioEntregado = envioRepository.save(envio);

        return mapperEnvio.toDto(envioEntregado);
        
    }
}