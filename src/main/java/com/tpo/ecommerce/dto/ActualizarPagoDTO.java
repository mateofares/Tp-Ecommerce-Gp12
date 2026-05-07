package com.tpo.ecommerce.dto;

import com.tpo.ecommerce.enums.EstadoPago;
import com.tpo.ecommerce.enums.MetodoPago;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ActualizarPagoDTO {
    private MetodoPago metodo;
    private EstadoPago estado;
}
