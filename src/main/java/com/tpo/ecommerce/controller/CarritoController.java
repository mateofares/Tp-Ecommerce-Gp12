package com.tpo.ecommerce.controller;

import com.tpo.ecommerce.dto.CarritoDTO;
import com.tpo.ecommerce.dto.OrdenDTO;
import com.tpo.ecommerce.service.CarritoService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/carrito")
public class CarritoController {
    
    private CarritoService carritoService;
    
    @PostMapping("/agregar")
    public CarritoDTO agregar(@RequestBody CarritoDTO dto) {
        return carritoService.agregar(dto);
    }
    
    @DeleteMapping("/eliminar")
    public CarritoDTO eliminar(@RequestBody CarritoDTO dto) {
        return carritoService.eliminar(dto);
    }
    
    @DeleteMapping("/vaciar")
    public CarritoDTO vaciar(@RequestBody CarritoDTO dto) {
        return carritoService.vaciar(dto);
    }
    
    @GetMapping("/{compradorId}")
    public CarritoDTO ver(@PathVariable Long compradorId) {
        return carritoService.ver(compradorId);
    }
    
    @PostMapping("/comprar")
    public OrdenDTO comprar(@RequestBody CarritoDTO dto) {
        return carritoService.comprar(dto);
    }
    
}