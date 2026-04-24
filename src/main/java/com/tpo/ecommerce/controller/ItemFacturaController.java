package com.tpo.ecommerce.controller;

import com.tpo.ecommerce.config.AuthorizationHelper;
import com.tpo.ecommerce.dto.ItemFacturaDTO;
import com.tpo.ecommerce.entity.Factura;
import com.tpo.ecommerce.entity.ItemFactura;
import com.tpo.ecommerce.exceptions.NotFoundException;
import com.tpo.ecommerce.repository.FacturaRepository;
import com.tpo.ecommerce.repository.ItemFacturaRepository;
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
    private final ItemFacturaRepository itemFacturaRepository;
    private final FacturaRepository facturaRepository;
    private final AuthorizationHelper authorizationHelper;

    @GetMapping("/{id}")
    public ResponseEntity<ItemFacturaDTO> obtenerPorId(@PathVariable Long id) {
        ItemFactura item = itemFacturaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Item factura no encontrado con id: " + id));
        authorizationHelper.authorize(item.getFactura().getOrden().getComprador().getId());
        ItemFacturaDTO itemDTO = itemFacturaService.obtenerPorId(id);
        return ResponseEntity.ok(itemDTO);
    }

    @GetMapping("/factura/{facturaId}")
    public ResponseEntity<List<ItemFacturaDTO>> obtenerPorFactura(@PathVariable Long facturaId) {
        Factura factura = facturaRepository.findById(facturaId)
                .orElseThrow(() -> new NotFoundException("Factura no encontrada con id: " + facturaId));
        authorizationHelper.authorize(factura.getOrden().getComprador().getId());
        List<ItemFacturaDTO> items = itemFacturaService.obtenerPorFactura(facturaId);
        return ResponseEntity.ok(items);
    }
}
