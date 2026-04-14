package com.tpo.ecommerce.controller;

import com.tpo.ecommerce.dto.ItemFacturaDTO;
import com.tpo.ecommerce.service.IItemFacturaService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/item-facturas")
@AllArgsConstructor
public class ItemFacturaController {

    private final IItemFacturaService itemFacturaService;

    @GetMapping("/{id}")
    public ResponseEntity<ItemFacturaDTO> obtenerPorId(@PathVariable Long id) {
        ItemFacturaDTO item = itemFacturaService.obtenerPorId(id);
        return ResponseEntity.ok(item);
    }

    @GetMapping("/factura/{facturaId}")
    public ResponseEntity<List<ItemFacturaDTO>> obtenerPorFactura(@PathVariable Long facturaId) {
        List<ItemFacturaDTO> items = itemFacturaService.obtenerPorFactura(facturaId);
        return ResponseEntity.ok(items);
    }
}
