package com.tpo.ecommerce.controller;

import com.tpo.ecommerce.dto.FacturaDTO;
import com.tpo.ecommerce.service.IFacturaService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/facturas")
@AllArgsConstructor
public class FacturaController {

    private final IFacturaService facturaService;

    @GetMapping("/{id}")
    public ResponseEntity<FacturaDTO> obtenerPorId(@PathVariable Long id) {
        FacturaDTO factura = facturaService.obtenerPorId(id);
        return ResponseEntity.ok(factura);
    }

    @GetMapping("/orden/{ordenId}")
    public ResponseEntity<FacturaDTO> obtenerPorOrden(@PathVariable Long ordenId) {
        FacturaDTO factura = facturaService.obtenerPorOrden(ordenId);
        return ResponseEntity.ok(factura);
    }

    @GetMapping("/{id}/descargar")
    public ResponseEntity<String> descargarPDF(@PathVariable Long id) {
        String urlPdf = facturaService.descargarPDF(id);
        return ResponseEntity.ok(urlPdf);
    }

    @PatchMapping("/{id}/anular")
    public ResponseEntity<Void> anularFactura(@PathVariable Long id) {
        facturaService.anularFactura(id);
        return ResponseEntity.noContent().build();
    }
}
