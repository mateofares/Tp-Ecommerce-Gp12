package com.tpo.ecommerce.controller;

import com.tpo.ecommerce.config.AuthorizationHelper;
import com.tpo.ecommerce.dto.ReseniaDTO;
import com.tpo.ecommerce.service.IReseniaService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/resenias")
@AllArgsConstructor
public class ReseniaController {

    private final IReseniaService reseniaService;
    private final AuthorizationHelper authorizationHelper;

    @PostMapping
    public ResponseEntity<ReseniaDTO> crear(@RequestBody ReseniaDTO dto) {
        dto.setCompradorId(authorizationHelper.getAuthenticatedUserId());
        return ResponseEntity.ok(reseniaService.crear(dto));
    }

    @GetMapping("/producto/{productoId}")
    public ResponseEntity<List<ReseniaDTO>> getByProducto(@PathVariable Long productoId) {
        return ResponseEntity.ok(reseniaService.getByProducto(productoId));
    }

    @GetMapping("/vendedor/{vendedorId}")
    public ResponseEntity<List<ReseniaDTO>> getByVendedor(@PathVariable Long vendedorId) {
        return ResponseEntity.ok(reseniaService.getByVendedor(vendedorId));
    }
}
