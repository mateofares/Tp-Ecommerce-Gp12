package com.tpo.ecommerce.controller;

import com.tpo.ecommerce.dto.OrdenDTO;
import com.tpo.ecommerce.service.IOrdenService;
import com.tpo.ecommerce.service.OrdenService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/ordenes")
public class OrdenController {
    
    private IOrdenService ordenService;
    
    @PostMapping
    public OrdenDTO comprar(@RequestBody OrdenDTO dto) {
        return ordenService.comprar(dto);
    }
    
    @GetMapping("/{compradorId}")
    public List<OrdenDTO> misCompras(@PathVariable Long compradorId) {
        return ordenService.misCompras(compradorId);
    }
    
    @GetMapping("/ventas/{vendedorId}")
    public List<OrdenDTO> misVentas(@PathVariable Long vendedorId) {
        return ordenService.misVentas(vendedorId);
    }

    @PostMapping("/{ordenId}/descuento/{descuentoId}")
    public OrdenDTO aplicarDescuento(
            @PathVariable Long ordenId,
            @PathVariable Long descuentoId
    ) {
        return ((OrdenService) ordenService).aplicarDescuentoAOrden(ordenId, descuentoId);
    }

    @DeleteMapping("/{ordenId}/descuento")
    public OrdenDTO removerDescuento(@PathVariable Long ordenId) {
        return ((OrdenService) ordenService).removerDescuentoDeOrden(ordenId);
    }
    
}