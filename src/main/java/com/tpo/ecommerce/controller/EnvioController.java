package com.tpo.ecommerce.controller;

import com.tpo.ecommerce.dto.EnvioDTO;
import com.tpo.ecommerce.enums.EstadoEnvio;
import com.tpo.ecommerce.service.IEnvioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/envios")
@AllArgsConstructor
public class EnvioController {
    
    private final IEnvioService envioService;
    
    @PostMapping
    public ResponseEntity<EnvioDTO> crearEnvio(@RequestBody EnvioDTO dto) {
        EnvioDTO envioCreado = envioService.crearEnvio(
            dto.getOrdenId(),
            dto.getTransportista(),
            dto.getNumSeguimiento(),
            dto.getFechaEstimada()
        );
        return ResponseEntity.status(201).body(envioCreado);
    }
    
    @GetMapping("/orden/{ordenId}")
    public ResponseEntity<EnvioDTO> obtenerPorOrden(@PathVariable Long ordenId) {
        EnvioDTO envio = envioService.obtenerPorOrden(ordenId);
        return ResponseEntity.ok(envio);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<EnvioDTO> obtenerPorId(@PathVariable Long id) {
        EnvioDTO envio = envioService.obtenerPorId(id);
        return ResponseEntity.ok(envio);
    }
    
    @GetMapping("/rastrear")
    public ResponseEntity<EnvioDTO> rastrearPaquete(@RequestParam String tracking) {
        EnvioDTO envio = envioService.obtenerPorTracking(tracking);
        return ResponseEntity.ok(envio);
    }
    
    @PatchMapping("/{id}/estado")
    public ResponseEntity<EnvioDTO> actualizarEstado(
            @PathVariable Long id,
            @RequestParam EstadoEnvio nuevoEstado) {
        EnvioDTO envioActualizado = envioService.actualizarEstado(id, nuevoEstado);
        return ResponseEntity.ok(envioActualizado);
    }
    
    @PatchMapping("/{id}/entregar")
    public ResponseEntity<EnvioDTO> registrarEntrega(
            @PathVariable Long id,
            @RequestParam LocalDateTime fechaEntrega) {
        EnvioDTO envioEntregado = envioService.registrarEntrega(id, fechaEntrega);
        return ResponseEntity.ok(envioEntregado);
    }
}