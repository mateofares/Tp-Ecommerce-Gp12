package com.tpo.ecommerce.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/productos")
public class ProductoController {
    
    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    private ProductoService productoService;
    
    @PostMapping
    public ProductoDTO publicar(@RequestBody ProductoDTO dto) {
        return productoService.publicar(dto);
    }
    
    @GetMapping
    public List<ProductoDTO> listarDisponibles() {
        return productoService.listarDisponibles();
    }
    
    @GetMapping("/{vendedorId}")
    public List<ProductoDTO> listarPorVendedor(@PathVariable Long vendedorId) {
        return productoService.listarPorVendedor(vendedorId);
    }
    
    @PutMapping("/{id}")
    public ProductoDTO actualizar(@PathVariable Long id, @RequestBody ProductoDTO dto) {
        return productoService.actualizar(id, dto);
    }
    
}   
