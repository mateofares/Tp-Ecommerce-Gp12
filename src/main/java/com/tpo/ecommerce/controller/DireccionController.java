package com.tpo.ecommerce.controller;

import com.tpo.ecommerce.dto.DireccionDTO;
import com.tpo.ecommerce.service.IDireccionService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/direcciones")
@AllArgsConstructor
public class DireccionController {

    private final IDireccionService direccionService;

    @PostMapping
    public ResponseEntity<DireccionDTO> crear(@RequestBody DireccionDTO dto) {
        DireccionDTO result = direccionService.crear(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DireccionDTO> obtener(@PathVariable Long id) {
        DireccionDTO result = direccionService.obtener(id);
        return ResponseEntity.ok(result);
    }

    @GetMapping
    public ResponseEntity<List<DireccionDTO>> obtenerPorUsuario(@RequestParam Long usuarioId) {
        List<DireccionDTO> result = direccionService.obtenerPorUsuario(usuarioId);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/activas")
    public ResponseEntity<List<DireccionDTO>> obtenerActivas(@RequestParam Long usuarioId) {
        List<DireccionDTO> result = direccionService.obtenerActivas(usuarioId);
        return ResponseEntity.ok(result);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<DireccionDTO> actualizar(@PathVariable Long id, @RequestBody DireccionDTO dto) {
        DireccionDTO result = direccionService.actualizar(id, dto);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarLogico(@PathVariable Long id) {
        direccionService.eliminarLogico(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/eliminar-logico")
    public ResponseEntity<Void> eliminarLogicoPatch(@PathVariable Long id) {
        direccionService.eliminarLogico(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/predeterminada")
    public ResponseEntity<DireccionDTO> establecerPredeterminada(@PathVariable Long id) {
        DireccionDTO result = direccionService.establecerPredeterminada(id);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/predeterminada")
    public ResponseEntity<DireccionDTO> obtenerPredeterminada(@RequestParam Long usuarioId) {
        DireccionDTO result = direccionService.obtenerPredeterminada(usuarioId);

        if (result == null) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(result);
    }
}
