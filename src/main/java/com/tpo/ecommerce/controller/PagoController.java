package com.tpo.ecommerce.controller;

import com.tpo.ecommerce.config.AuthorizationHelper;
import com.tpo.ecommerce.dto.ActualizarPagoDTO;
import com.tpo.ecommerce.dto.PagoDTO;
import com.tpo.ecommerce.dto.RealizarPagoDTO;
import com.tpo.ecommerce.entity.Orden;
import com.tpo.ecommerce.entity.Pago;
import com.tpo.ecommerce.exceptions.NotFoundException;
import com.tpo.ecommerce.repository.OrdenRepository;
import com.tpo.ecommerce.repository.PagoRepository;
import com.tpo.ecommerce.service.IPagoService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/pagos")
public class PagoController {

    private final IPagoService pagoService;
    private final PagoRepository pagoRepository;
    private final OrdenRepository ordenRepository;
    private final AuthorizationHelper authorizationHelper;

    @GetMapping("/{pagoId}")
    public PagoDTO obtenerPorId(@PathVariable Long pagoId) {
        authorizationHelper.authorize(getCompradorIdPorPago(pagoId));
        return pagoService.obtenerPorId(pagoId);
    }

    @GetMapping("/orden/{ordenId}")
    public PagoDTO obtenerPorOrden(@PathVariable Long ordenId) {
        authorizationHelper.authorize(getCompradorIdPorOrden(ordenId));
        return pagoService.obtenerPorOrden(ordenId);
    }

    @PostMapping("/orden/{ordenId}/pagar")
    public PagoDTO pagarOrden(@PathVariable Long ordenId, @RequestBody RealizarPagoDTO dto) {
        authorizationHelper.authorize(getCompradorIdPorOrden(ordenId));
        return pagoService.pagarOrden(ordenId, dto);
    }

    @PatchMapping("/{pagoId}")
    public PagoDTO actualizarPago(@PathVariable Long pagoId, @RequestBody ActualizarPagoDTO dto) {
        authorizationHelper.requireAdmin();
        return pagoService.actualizarPago(pagoId, dto);
    }

    private Long getCompradorIdPorPago(Long pagoId) {
        Pago pago = pagoRepository.findById(pagoId)
                .orElseThrow(() -> new NotFoundException("Pago no encontrado"));
        return pago.getOrden().getComprador().getId();
    }

    private Long getCompradorIdPorOrden(Long ordenId) {
        Orden orden = ordenRepository.findById(ordenId)
                .orElseThrow(() -> new NotFoundException("Orden no encontrada"));
        return orden.getComprador().getId();
    }
}
