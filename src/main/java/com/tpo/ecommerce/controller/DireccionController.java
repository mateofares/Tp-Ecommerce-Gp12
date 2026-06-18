package com.tpo.ecommerce.controller;

import com.tpo.ecommerce.config.AuthorizationHelper;
import com.tpo.ecommerce.dto.DireccionDTO;
import com.tpo.ecommerce.entity.Direccion;
import com.tpo.ecommerce.exceptions.NotFoundException;
import com.tpo.ecommerce.repository.DireccionRepository;
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
    private final DireccionRepository direccionRepository;
    private final AuthorizationHelper authorizationHelper;

    @PostMapping
    public ResponseEntity<DireccionDTO> crear(@RequestBody DireccionDTO dto) {
        dto.setUsuarioId(authorizationHelper.getAuthenticatedUserId());
        DireccionDTO result = direccionService.crear(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DireccionDTO> obtener(@PathVariable Long id) {
        authorizationHelper.authorize(getOwnerUserId(id));
        DireccionDTO result = direccionService.obtener(id);
        return ResponseEntity.ok(result);
    }

    @GetMapping
    public ResponseEntity<List<DireccionDTO>> obtenerPorUsuario() {
        Long usuarioId = authorizationHelper.getAuthenticatedUserId();
        List<DireccionDTO> result = direccionService.obtenerPorUsuario(usuarioId);
        return ResponseEntity.ok(result);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<DireccionDTO> actualizar(@PathVariable Long id, @RequestBody DireccionDTO dto) {
        authorizationHelper.authorize(getOwnerUserId(id));
        DireccionDTO result = direccionService.actualizar(id, dto);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarLogico(@PathVariable Long id) {
        authorizationHelper.authorize(getOwnerUserId(id));
        direccionService.eliminarLogico(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/predeterminada")
    public ResponseEntity<DireccionDTO> establecerPredeterminada(@PathVariable Long id) {
        authorizationHelper.authorize(getOwnerUserId(id));
        DireccionDTO result = direccionService.establecerPredeterminada(id);
        return ResponseEntity.ok(result);
    }

    private Long getOwnerUserId(Long direccionId) {
        Direccion direccion = direccionRepository.findById(direccionId)
                .orElseThrow(() -> new NotFoundException("Dirección no encontrada"));
        return direccion.getUsuario().getId();
    }
}
