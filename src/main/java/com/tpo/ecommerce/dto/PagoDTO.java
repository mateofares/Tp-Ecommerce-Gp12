package com.tpo.ecommerce.dto;

import com.tpo.ecommerce.enums.EstadoPago;
import com.tpo.ecommerce.enums.MetodoPago;
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
    private MetodoPago metodo;
    private EstadoPago estado;
    private Double monto;
    private LocalDateTime fechaPago;
}
