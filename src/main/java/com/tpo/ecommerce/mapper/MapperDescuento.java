package com.tpo.ecommerce.mapper;

import com.tpo.ecommerce.dto.DescuentoDTO;
import com.tpo.ecommerce.entity.Descuento;
import org.springframework.stereotype.Component;

@Component


public class MapperDescuento {

    public DescuentoDTO toDto(Descuento descuento) {
        return new DescuentoDTO(
                descuento.getId(),
                descuento.getCodigoDescuento(),
                descuento.getTipo(),
                descuento.getValor(),
                descuento.getValidoDesde(),
                descuento.getValidoHasta(),
                descuento.getMaximoDeUsos(),
                descuento.getUsosActuales()
        );
    }

    public Descuento toEntity(DescuentoDTO descuentoDTO) {
        return new Descuento(
                descuentoDTO.getCodigoDescuento(),
                descuentoDTO.getTipo(),
                descuentoDTO.getValor(),
                descuentoDTO.getValidoDesde(),
                descuentoDTO.getValidoHasta(),
                descuentoDTO.getMaximoDeUsos()
        );
    }
}
