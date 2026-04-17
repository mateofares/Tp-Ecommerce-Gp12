package com.tpo.ecommerce.controller;

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

    @PostMapping
    public ResponseEntity<ReseniaDTO> crear(@RequestBody ReseniaDTO dto) {
        return ResponseEntity.ok(reseniaService.crear(dto));
    }

    @GetMapping("/producto/{productoId}")
    public ResponseEntity<List<ReseniaDTO>> getByProducto(@PathVariable Long productoId) {
        return ResponseEntity.ok(reseniaService.getByProducto(productoId));
    }
}
