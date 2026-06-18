package com.tpo.ecommerce.controller;

import com.tpo.ecommerce.config.AuthorizationHelper;
import com.tpo.ecommerce.dto.FacturaDTO;
import com.tpo.ecommerce.entity.Factura;
import com.tpo.ecommerce.exceptions.NotFoundException;
import com.tpo.ecommerce.repository.FacturaRepository;
import com.tpo.ecommerce.service.IFacturaService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/facturas")
@AllArgsConstructor
public class FacturaController {

    private final IFacturaService facturaService;
    private final FacturaRepository facturaRepository;
    private final AuthorizationHelper authorizationHelper;

    @GetMapping
    public ResponseEntity<List<FacturaDTO>> listarTodas() {
        authorizationHelper.requireAdmin();
        return ResponseEntity.ok(facturaService.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FacturaDTO> obtenerPorId(@PathVariable Long id) {
        authorizationHelper.authorize(getCompradorId(id));
        FacturaDTO factura = facturaService.obtenerPorId(id);
        return ResponseEntity.ok(factura);
    }

    @GetMapping("/orden/{ordenId}")
    public ResponseEntity<FacturaDTO> obtenerPorOrden(@PathVariable Long ordenId) {
        Factura factura = facturaRepository.findByOrdenId(ordenId)
                .orElseThrow(() -> new NotFoundException("Factura no encontrada para la orden: " + ordenId));
        authorizationHelper.authorize(factura.getOrden().getComprador().getId());
        FacturaDTO facturaDTO = facturaService.obtenerPorOrden(ordenId);
        return ResponseEntity.ok(facturaDTO);
    }

    @GetMapping("/{id}/descargar")
    public ResponseEntity<String> descargarPDF(@PathVariable Long id) {
        authorizationHelper.authorize(getCompradorId(id));
        String urlPdf = facturaService.descargarPDF(id);
        return ResponseEntity.ok(urlPdf);
    }

    @PatchMapping("/{id}/anular")
    public ResponseEntity<Void> anularFactura(@PathVariable Long id) {
        facturaService.anularFactura(id);
        return ResponseEntity.noContent().build();
    }

    private Long getCompradorId(Long facturaId) {
        Factura factura = facturaRepository.findById(facturaId)
                .orElseThrow(() -> new NotFoundException("Factura no encontrada con id: " + facturaId));
        return factura.getOrden().getComprador().getId();
    }
}
