package com.tpo.ecommerce.controller;

import com.tpo.ecommerce.config.AuthorizationHelper;
import com.tpo.ecommerce.dto.AplicarDescuentoDTO;
import com.tpo.ecommerce.dto.ProductoDTO;
import com.tpo.ecommerce.entity.Producto;
import com.tpo.ecommerce.enums.Categorias;
import com.tpo.ecommerce.enums.Color;
import com.tpo.ecommerce.enums.Estado;
import com.tpo.ecommerce.enums.Marca;
import com.tpo.ecommerce.enums.Talle;
import com.tpo.ecommerce.exceptions.NotFoundException;
import com.tpo.ecommerce.repository.ProductoRepository;
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
    private final ProductoRepository productoRepository;
    private final AuthorizationHelper authorizationHelper;

    @PostMapping
    public ResponseEntity<ProductoDTO> crear(@RequestBody ProductoDTO dto) {
        dto.setUsuarioId(authorizationHelper.getAuthenticatedUserId());
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
        authorizationHelper.authorize(getOwnerUserId(id));
        productoService.deleteProducto(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/eliminar-logico")
    public ResponseEntity<Void> eliminarLogico(@PathVariable Long id) {
        authorizationHelper.authorize(getOwnerUserId(id));
        productoService.eliminarLogico(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ProductoDTO> actualizar(@PathVariable Long id, @RequestBody ProductoDTO dto) {
        authorizationHelper.authorize(getOwnerUserId(id));
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

    @PatchMapping("/{productoId}/descuento")
    public ResponseEntity<ProductoDTO> aplicarDescuentoPorPorcentaje(
            @PathVariable Long productoId,
            @RequestBody AplicarDescuentoDTO dto
    ) {
        Long vendedorId = authorizationHelper.getAuthenticatedUserId();
        authorizationHelper.authorize(vendedorId);
        return ResponseEntity.ok(productoService.aplicarDescuentoPorPorcentaje(productoId, dto.getPorcentaje(), vendedorId));
    }

    @PatchMapping("/{productoId}/descuento/{descuentoId}")
    public ResponseEntity<ProductoDTO> aplicarDescuento(
            @PathVariable Long productoId,
            @PathVariable Long descuentoId
    ) {
        Long vendedorId = authorizationHelper.getAuthenticatedUserId();
        authorizationHelper.authorize(vendedorId);
        return ResponseEntity.ok(productoService.aplicarDescuento(productoId, descuentoId, vendedorId));
    }

    @DeleteMapping("/{productoId}/descuento")
    public ResponseEntity<ProductoDTO> removerDescuento(@PathVariable Long productoId) {
        Long vendedorId = authorizationHelper.getAuthenticatedUserId();
        authorizationHelper.authorize(vendedorId);
        return ResponseEntity.ok(productoService.removerDescuento(productoId, vendedorId));
    }

    private Long getOwnerUserId(Long productoId) {
        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new NotFoundException("Producto no encontrado"));
        if (producto.getUsuario() == null) {
            throw new NotFoundException("El producto no tiene propietario asignado");
        }
        return producto.getUsuario().getId();
    }
}
