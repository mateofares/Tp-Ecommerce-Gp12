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

    // Solo el vendedor de la orden (o admin) puede crear el envío
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

    // El comprador puede ver su envío, el vendedor también
    @GetMapping("/orden/{ordenId}")
    public ResponseEntity<EnvioDTO> obtenerPorOrden(@PathVariable Long ordenId) {
        authorizationHelper.authorizeCompradorOVendedorDeOrden(getOrden(ordenId));
        EnvioDTO envio = envioService.obtenerPorOrden(ordenId);
        return ResponseEntity.ok(envio);
    }

    // El comprador puede ver su envío, el vendedor también
    @GetMapping("/{id}")
    public ResponseEntity<EnvioDTO> obtenerPorId(@PathVariable Long id) {
        authorizationHelper.authorizeCompradorOVendedorDeOrden(getEnvio(id).getOrden());
        EnvioDTO envio = envioService.obtenerPorId(id);
        return ResponseEntity.ok(envio);
    }

    // Cualquier usuario autenticado puede rastrear con el número de seguimiento
    @GetMapping("/rastrear")
    public ResponseEntity<EnvioDTO> rastrearPaquete(@RequestParam String tracking) {
        EnvioDTO envio = envioService.obtenerPorTracking(tracking);
        return ResponseEntity.ok(envio);
    }

    // Solo el vendedor (o admin) puede cambiar el estado del envío
    @PatchMapping("/{id}/estado")
    public ResponseEntity<EnvioDTO> actualizarEstado(
            @PathVariable Long id,
            @RequestParam EstadoEnvio nuevoEstado) {
        authorizationHelper.authorizeVendedorDeOrden(getEnvio(id).getOrden());
        EnvioDTO envioActualizado = envioService.actualizarEstado(id, nuevoEstado);
        return ResponseEntity.ok(envioActualizado);
    }

    // Solo el vendedor (o admin) puede registrar la entrega
    @PatchMapping("/{id}/entregar")
    public ResponseEntity<EnvioDTO> registrarEntrega(
            @PathVariable Long id,
            @RequestParam LocalDateTime fechaEntrega) {
        authorizationHelper.authorizeVendedorDeOrden(getEnvio(id).getOrden());
        EnvioDTO envioEntregado = envioService.registrarEntrega(id, fechaEntrega);
        return ResponseEntity.ok(envioEntregado);
    }

    private Orden getOrden(Long ordenId) {
        return ordenRepository.findById(ordenId)
                .orElseThrow(() -> new NotFoundException("Orden no encontrada"));
    }

    private Envio getEnvio(Long envioId) {
        return envioRepository.findById(envioId)
                .orElseThrow(() -> new NotFoundException("Envío no encontrado"));
    }
}
