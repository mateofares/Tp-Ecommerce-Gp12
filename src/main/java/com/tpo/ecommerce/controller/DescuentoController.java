package com.tpo.ecommerce.controller;

import com.tpo.ecommerce.dto.DescuentoDTO;
import com.tpo.ecommerce.service.IDescuentoService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/descuentos")
@AllArgsConstructor


public class DescuentoController {
    private final IDescuentoService descuentoService;

    @GetMapping
    public ResponseEntity<List<DescuentoDTO>> getDescuentos(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String codigoDescuento,
            @RequestParam(required = false) String tipo
    ) {
        return ResponseEntity.ok(descuentoService.getDescuentos(id, codigoDescuento, tipo));
    }

    @GetMapping("/codigo/{codigo}")
    public ResponseEntity<DescuentoDTO> getDescuentoPorCodigo(@PathVariable String codigo) {
        return ResponseEntity.ok(descuentoService.getDescuentoPorCodigo(codigo));
    }

    @PostMapping
    public ResponseEntity<DescuentoDTO> createDescuento(@RequestBody DescuentoDTO descuentoDTO) {
        return ResponseEntity.ok(descuentoService.createDescuento(descuentoDTO));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<DescuentoDTO> updateDescuento(
            @PathVariable Long id,
            @RequestBody DescuentoDTO descuentoDTO
    ) {
        return ResponseEntity.ok(descuentoService.updateDescuento(id, descuentoDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDescuento(@PathVariable Long id) {
        descuentoService.deleteDescuento(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/calcular")
    public ResponseEntity<Double> calcularDescuento(
            @PathVariable Long id,
            @RequestParam Double monto
    ) {
        Double descuento = descuentoService.calcularDescuento(monto, id);
        return ResponseEntity.ok(descuento);
    }

    @PostMapping("/{id}/precio-final")
    public ResponseEntity<Double> calcularPrecioFinal(
            @PathVariable Long id,
            @RequestParam Double monto
    ) {
        Double precioFinal = descuentoService.calcularPrecioFinal(monto, id);
        return ResponseEntity.ok(precioFinal);
    }

    @GetMapping("/{id}/valido")
    public ResponseEntity<Boolean> validarDescuentoAplicable(@PathVariable Long id) {
        boolean valido = descuentoService.validarDescuentoAplicable(id);
        return ResponseEntity.ok(valido);
    }
}
