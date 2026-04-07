package com.tpo.ecommerce.dto;

import com.tpo.ecommerce.enums.EstadoPago;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PagoDTO {
    private Long id;
    private Long ordenId;
    private String metodo;
    private EstadoPago estado;
    private Double monto;
    private String referenciaMaxima;
    private LocalDateTime fechaPago;
}
