package com.tpo.ecommerce.controller;

import com.tpo.ecommerce.dto.ProductoDTO;
import com.tpo.ecommerce.enums.Categorias;
import com.tpo.ecommerce.enums.Color;
import com.tpo.ecommerce.enums.Estado;
import com.tpo.ecommerce.enums.Marca;
import com.tpo.ecommerce.enums.Talle;
import com.tpo.ecommerce.service.IProductoService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/productos")
@AllArgsConstructor
public class ProductoController {
    private final IProductoService productoService;

    @PostMapping
    public ResponseEntity<ProductoDTO> crear(@RequestBody ProductoDTO dto) {
        return ResponseEntity.ok(productoService.crear(dto));
    }

    @GetMapping
    public ResponseEntity<List<ProductoDTO>> getProductos(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) Long usuarioId,
            @RequestParam(required = false) String titulo,
            @RequestParam(required = false) String descripcion,
            @RequestParam(required = false) Double precio,
            @RequestParam(required = false) Categorias categoria,
            @RequestParam(required = false) Marca marca,
            @RequestParam(required = false) Talle talle,
            @RequestParam(required = false) Color color,
            @RequestParam(required = false) Estado estado
    ) {
        return ResponseEntity.ok(productoService.getProductos(
                id, usuarioId, titulo, descripcion, precio, categoria, marca, talle, color, estado
        ));
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteProducto(@RequestParam Long id) {
        productoService.deleteProducto(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/eliminar-logico")
    public ResponseEntity<Void> eliminarLogico(@PathVariable Long id) {
        productoService.eliminarLogico(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ProductoDTO> actualizar(@PathVariable Long id, @RequestBody ProductoDTO dto) {
        return ResponseEntity.ok(productoService.actualizar(
                id,
                dto.getTitulo(),
                dto.getDescripcion(),
                dto.getPrecio(),
                dto.getCategoria(),
                dto.getMarca(),
                dto.getTalle(),
                dto.getColor(),
                dto.getEstado(),
                dto.getImagenUrl()
        ));
    }

    @PatchMapping("/{productoId}/descuento/{descuentoId}")
    public ResponseEntity<ProductoDTO> aplicarDescuento(
            @PathVariable Long productoId,
            @PathVariable Long descuentoId,
            @RequestParam Long vendedorId
    ) {
        return ResponseEntity.ok(productoService.aplicarDescuento(productoId, descuentoId, vendedorId));
    }

    @DeleteMapping("/{productoId}/descuento")
    public ResponseEntity<ProductoDTO> removerDescuento(
            @PathVariable Long productoId,
            @RequestParam Long vendedorId
    ) {
        return ResponseEntity.ok(productoService.removerDescuento(productoId, vendedorId));
    }
}
