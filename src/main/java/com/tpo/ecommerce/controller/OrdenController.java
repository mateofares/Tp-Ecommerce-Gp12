package com.tpo.ecommerce.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ordenes")
public class OrdenController {
    
    @Autowired
    private OrdenService ordenService;
    
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
    
}