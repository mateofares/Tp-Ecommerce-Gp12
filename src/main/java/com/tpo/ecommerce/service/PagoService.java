package com.tpo.ecommerce.service;

import com.tpo.ecommerce.dto.ActualizarPagoDTO;
import com.tpo.ecommerce.dto.PagoDTO;
import com.tpo.ecommerce.dto.RealizarPagoDTO;
import com.tpo.ecommerce.entity.Orden;
import com.tpo.ecommerce.entity.Pago;
import com.tpo.ecommerce.enums.EstadoOrden;
import com.tpo.ecommerce.enums.EstadoPago;
import com.tpo.ecommerce.exceptions.BadRequestException;
import com.tpo.ecommerce.exceptions.DuplicateResourceException;
import com.tpo.ecommerce.exceptions.NotFoundException;
import com.tpo.ecommerce.mapper.MapperPago;
import com.tpo.ecommerce.repository.FacturaRepository;
import com.tpo.ecommerce.repository.OrdenRepository;
import com.tpo.ecommerce.repository.PagoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class PagoService implements IPagoService {

    private static final String METODO_A_DEFINIR = "A_DEFINIR";

    private final PagoRepository pagoRepository;
    private final OrdenRepository ordenRepository;
    private final FacturaRepository facturaRepository;
    private final IFacturaService facturaService;
    private final MapperPago mapperPago;

    @Override
    @Transactional
    public PagoDTO crearPendientePorOrden(Long ordenId) {
        if (ordenId == null) {
            throw new BadRequestException("Debe indicar el ordenId");
        }
        if (pagoRepository.findByOrdenId(ordenId).isPresent()) {
            throw new DuplicateResourceException("Pago", "ordenId", ordenId);
        }

        Orden orden = ordenRepository.findById(ordenId)
                .orElseThrow(() -> new NotFoundException("Orden no encontrada"));

        Pago pago = new Pago();
        pago.setOrden(orden);
        pago.setMetodo(METODO_A_DEFINIR);
        pago.setEstado(EstadoPago.PENDIENTE);
        pago.setMonto(orden.getTotal());
        pago.setReferenciaMaxima(generarReferenciaMaxima(ordenId));
        pago.setFechaPago(LocalDateTime.now());

        Pago pagoGuardado = pagoRepository.save(pago);
        generarFacturaSiCorresponde(pagoGuardado);
        return mapperPago.toDto(pagoGuardado);
    }

    @Override
    @Transactional(readOnly = true)
    public PagoDTO obtenerPorId(Long pagoId) {
        if (pagoId == null) {
            throw new BadRequestException("Debe indicar el pagoId");
        }
        Pago pago = pagoRepository.findById(pagoId)
                .orElseThrow(() -> new NotFoundException("Pago no encontrado"));
        return mapperPago.toDto(pago);
    }

    @Override
    @Transactional(readOnly = true)
    public PagoDTO obtenerPorOrden(Long ordenId) {
        if (ordenId == null) {
            throw new BadRequestException("Debe indicar el ordenId");
        }
        Pago pago = pagoRepository.findByOrdenId(ordenId)
                .orElseThrow(() -> new NotFoundException("Pago no encontrado para la orden"));
        return mapperPago.toDto(pago);
    }

    @Override
    @Transactional
    public PagoDTO pagarOrden(Long ordenId, RealizarPagoDTO dto) {
        if (ordenId == null) {
            throw new BadRequestException("Debe indicar el ordenId");
        }
        if (dto == null) {
            throw new BadRequestException("Debe enviar datos del pago");
        }
        if (!StringUtils.hasText(dto.getMetodo())) {
            throw new BadRequestException("Debe indicar el metodo de pago");
        }
        if (!StringUtils.hasText(dto.getReferenciaMaxima())) {
            throw new BadRequestException("Debe indicar la referencia de pago");
        }

        Pago pago = pagoRepository.findByOrdenId(ordenId)
                .orElseThrow(() -> new NotFoundException("Pago no encontrado para la orden"));

        if (pago.getEstado() == EstadoPago.APROBADO) {
            throw new BadRequestException("El pago ya fue aprobado");
        }
        if (pago.getEstado() == EstadoPago.RECHAZADO || pago.getEstado() == EstadoPago.CANCELADO) {
            throw new BadRequestException("No se puede pagar una orden con pago rechazado o cancelado");
        }

        pago.setMetodo(dto.getMetodo().trim());
        pago.setReferenciaMaxima(dto.getReferenciaMaxima().trim());
        pago.setEstado(EstadoPago.APROBADO);
        pago.setFechaPago(LocalDateTime.now());
        sincronizarEstadoOrden(pago.getOrden(), EstadoPago.APROBADO);

        Pago pagoGuardado = pagoRepository.save(pago);
        generarFacturaSiCorresponde(pagoGuardado);
        return mapperPago.toDto(pagoGuardado);
    }

    @Override
    @Transactional
    public PagoDTO actualizarPago(Long pagoId, ActualizarPagoDTO dto) {
        if (pagoId == null) {
            throw new BadRequestException("Debe indicar el pagoId");
        }
        if (dto == null) {
            throw new BadRequestException("Debe enviar datos para actualizar el pago");
        }

        Pago pago = pagoRepository.findById(pagoId)
                .orElseThrow(() -> new NotFoundException("Pago no encontrado"));

        boolean hayCambios = false;

        if (StringUtils.hasText(dto.getMetodo())) {
            pago.setMetodo(dto.getMetodo().trim());
            hayCambios = true;
        }

        if (StringUtils.hasText(dto.getReferenciaMaxima())) {
            pago.setReferenciaMaxima(dto.getReferenciaMaxima().trim());
            hayCambios = true;
        }

        if (dto.getEstado() != null) {
            pago.setEstado(dto.getEstado());
            pago.setFechaPago(LocalDateTime.now());
            sincronizarEstadoOrden(pago.getOrden(), dto.getEstado());
            hayCambios = true;
        }

        if (!hayCambios) {
            throw new BadRequestException("Debe enviar al menos un campo para actualizar");
        }

        return mapperPago.toDto(pagoRepository.save(pago));
    }

    private void sincronizarEstadoOrden(Orden orden, EstadoPago estadoPago) {
        if (estadoPago == EstadoPago.APROBADO) {
            orden.setEstado(EstadoOrden.PAGADA);
            return;
        }
        if (estadoPago == EstadoPago.RECHAZADO || estadoPago == EstadoPago.CANCELADO) {
            orden.setEstado(EstadoOrden.CANCELADA);
            return;
        }
        orden.setEstado(EstadoOrden.CONFIRMADA);
    }

    private String generarReferenciaMaxima(Long ordenId) {
        return "ORD-" + ordenId + "-" + System.currentTimeMillis();
    }

    private void generarFacturaSiCorresponde(Pago pago) {
        if (pago.getEstado() != EstadoPago.APROBADO || pago.getOrden() == null || pago.getOrden().getId() == null) {
            return;
        }

        Long ordenId = pago.getOrden().getId();
        if (facturaRepository.findByOrdenId(ordenId).isEmpty()) {
            facturaService.crearFacturaAutomatica(ordenId);
        }
    }
}
