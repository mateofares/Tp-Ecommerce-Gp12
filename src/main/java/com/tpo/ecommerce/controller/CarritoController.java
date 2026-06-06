package com.tpo.ecommerce.controller;

import com.tpo.ecommerce.config.AuthorizationHelper;
import com.tpo.ecommerce.dto.CarritoDTO;
import com.tpo.ecommerce.dto.OrdenDTO;
import com.tpo.ecommerce.service.ICarritoService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/carrito")
public class CarritoController {

    private final ICarritoService carritoService;
    private final AuthorizationHelper authorizationHelper;

    @PostMapping("/agregar")
    public CarritoDTO agregar(@RequestBody CarritoDTO dto) {
        dto.setCompradorId(authorizationHelper.getAuthenticatedUserId());
        return carritoService.agregar(dto);
    }

    @DeleteMapping("/eliminar")
    public CarritoDTO eliminar(@RequestBody CarritoDTO dto) {
        dto.setCompradorId(authorizationHelper.getAuthenticatedUserId());
        return carritoService.eliminar(dto);
    }

    @DeleteMapping("/vaciar")
    public CarritoDTO vaciar() {
        Long compradorId = authorizationHelper.getAuthenticatedUserId();
        CarritoDTO dto = new CarritoDTO();
        dto.setCompradorId(compradorId);
        return carritoService.vaciar(dto);
    }

    @GetMapping
    public CarritoDTO ver() {
        return carritoService.ver(authorizationHelper.getAuthenticatedUserId());
    }

    @PostMapping("/comprar")
    public OrdenDTO comprar(@RequestBody CarritoDTO dto) {
        dto.setCompradorId(authorizationHelper.getAuthenticatedUserId());
        return carritoService.comprar(dto);
    }

    @PostMapping("/descuento/{descuentoId}")
    public CarritoDTO aplicarDescuento(@PathVariable Long descuentoId) {
        return carritoService.aplicarDescuentoAlCarrito(
                authorizationHelper.getAuthenticatedUserId(), descuentoId);
    }

    @DeleteMapping("/descuento")
    public CarritoDTO removerDescuento() {
        return carritoService.removerDescuentoDelCarrito(
                authorizationHelper.getAuthenticatedUserId());
    }
}
