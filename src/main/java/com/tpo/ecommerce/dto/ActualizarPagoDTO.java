package com.tpo.ecommerce.dto;

import com.tpo.ecommerce.enums.EstadoPago;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ActualizarPagoDTO {
    private String metodo;
    private EstadoPago estado;
    private String referenciaMaxima;
}
