package com.tpo.ecommerce.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/carrito")
public class CarritoController {
    
    @Autowired
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