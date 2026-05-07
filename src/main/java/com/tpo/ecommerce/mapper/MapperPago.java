package com.tpo.ecommerce.mapper;

import com.tpo.ecommerce.dto.PagoDTO;
import com.tpo.ecommerce.entity.Pago;
import org.springframework.stereotype.Component;

@Component
public class MapperPago {

    public PagoDTO toDto(Pago pago) {
        if (pago == null) return null;
        return new PagoDTO(
                pago.getId(),
                pago.getOrden() != null ? pago.getOrden().getId() : null,
                pago.getMetodo(),
                pago.getEstado(),
                pago.getMonto(),
                pago.getFechaPago()
        );
    }
}
