package com.tpo.ecommerce.controller;

import com.tpo.ecommerce.config.AuthorizationHelper;
import com.tpo.ecommerce.dto.EnvioDTO;
import com.tpo.ecommerce.entity.Envio;
import com.tpo.ecommerce.entity.Orden;
import com.tpo.ecommerce.enums.EstadoEnvio;
import com.tpo.ecommerce.exceptions.NotFoundException;
import com.tpo.ecommerce.repository.EnvioRepository;
import com.tpo.ecommerce.repository.OrdenRepository;
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
    private final EnvioRepository envioRepository;
    private final OrdenRepository ordenRepository;
    private final AuthorizationHelper authorizationHelper;

    @PostMapping
    public ResponseEntity<EnvioDTO> crearEnvio(@RequestBody EnvioDTO dto) {
        authorizationHelper.requireAdmin();
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
        authorizationHelper.authorize(getCompradorIdPorOrden(ordenId));
        EnvioDTO envio = envioService.obtenerPorOrden(ordenId);
        return ResponseEntity.ok(envio);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EnvioDTO> obtenerPorId(@PathVariable Long id) {
        authorizationHelper.authorize(getCompradorIdPorEnvio(id));
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
        authorizationHelper.requireAdmin();
        EnvioDTO envioEntregado = envioService.registrarEntrega(id, fechaEntrega);
        return ResponseEntity.ok(envioEntregado);
    }

    private Long getCompradorIdPorOrden(Long ordenId) {
        Orden orden = ordenRepository.findById(ordenId)
                .orElseThrow(() -> new NotFoundException("Orden no encontrada"));
        return orden.getComprador().getId();
    }

    private Long getCompradorIdPorEnvio(Long envioId) {
        Envio envio = envioRepository.findById(envioId)
                .orElseThrow(() -> new NotFoundException("Envío no encontrado"));
        return envio.getOrden().getComprador().getId();
    }
}
