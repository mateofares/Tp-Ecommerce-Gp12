package com.tpo.ecommerce.controller;

import com.tpo.ecommerce.dto.ActualizarPagoDTO;
import com.tpo.ecommerce.dto.PagoDTO;
import com.tpo.ecommerce.dto.RealizarPagoDTO;
import com.tpo.ecommerce.service.IPagoService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/pagos")
public class PagoController {

    private final IPagoService pagoService;

    @GetMapping("/{pagoId}")
    public PagoDTO obtenerPorId(@PathVariable Long pagoId) {
        return pagoService.obtenerPorId(pagoId);
    }

    @GetMapping("/orden/{ordenId}")
    public PagoDTO obtenerPorOrden(@PathVariable Long ordenId) {
        return pagoService.obtenerPorOrden(ordenId);
    }

    @PostMapping("/orden/{ordenId}/pagar")
    public PagoDTO pagarOrden(@PathVariable Long ordenId, @RequestBody RealizarPagoDTO dto) {
        return pagoService.pagarOrden(ordenId, dto);
    }

    @PatchMapping("/{pagoId}")
    public PagoDTO actualizarPago(@PathVariable Long pagoId, @RequestBody ActualizarPagoDTO dto) {
        return pagoService.actualizarPago(pagoId, dto);
    }
}
