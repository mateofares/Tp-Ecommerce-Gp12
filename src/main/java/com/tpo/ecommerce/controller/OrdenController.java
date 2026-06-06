package com.tpo.ecommerce.controller;

import com.tpo.ecommerce.config.AuthorizationHelper;
import com.tpo.ecommerce.dto.OrdenDTO;
import com.tpo.ecommerce.entity.Orden;
import com.tpo.ecommerce.exceptions.NotFoundException;
import com.tpo.ecommerce.repository.OrdenRepository;
import com.tpo.ecommerce.service.IOrdenService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/ordenes")
public class OrdenController {

    private final IOrdenService ordenService;
    private final OrdenRepository ordenRepository;
    private final AuthorizationHelper authorizationHelper;

    @PostMapping
    public OrdenDTO comprar(@RequestBody OrdenDTO dto) {
        dto.setCompradorId(authorizationHelper.getAuthenticatedUserId());
        return ordenService.comprar(dto);
    }

    @GetMapping("/mis-compras")
    public List<OrdenDTO> misCompras() {
        return ordenService.misCompras(authorizationHelper.getAuthenticatedUserId());
    }

    @GetMapping("/mis-ventas")
    public List<OrdenDTO> misVentas() {
        return ordenService.misVentas(authorizationHelper.getAuthenticatedUserId());
    }

    @PostMapping("/{ordenId}/descuento/{descuentoId}")
    public OrdenDTO aplicarDescuento(
            @PathVariable Long ordenId,
            @PathVariable Long descuentoId
    ) {
        authorizationHelper.authorize(getCompradorId(ordenId));
        return ordenService.aplicarDescuentoAOrden(ordenId, descuentoId);
    }

    @DeleteMapping("/{ordenId}/descuento")
    public OrdenDTO removerDescuento(@PathVariable Long ordenId) {
        authorizationHelper.authorize(getCompradorId(ordenId));
        return ordenService.removerDescuentoDeOrden(ordenId);
    }

    private Long getCompradorId(Long ordenId) {
        Orden orden = ordenRepository.findById(ordenId)
                .orElseThrow(() -> new NotFoundException("Orden no encontrada"));
        return orden.getComprador().getId();
    }
}
