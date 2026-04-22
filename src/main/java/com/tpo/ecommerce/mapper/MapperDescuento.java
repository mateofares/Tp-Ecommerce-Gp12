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
                descuento.getValidoHasta()
        );
    }

    public Descuento toEntity(DescuentoDTO dto) {
        return new Descuento(
                dto.getCodigoDescuento(),
                dto.getTipo(),
                dto.getValor(),
                dto.getValidoDesde(),
                dto.getValidoHasta()
        );
    }
}
