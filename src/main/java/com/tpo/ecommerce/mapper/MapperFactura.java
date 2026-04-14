package com.tpo.ecommerce.mapper;

import com.tpo.ecommerce.dto.FacturaDTO;
import com.tpo.ecommerce.entity.Factura;
import com.tpo.ecommerce.entity.Orden;
import org.springframework.stereotype.Component;

@Component
public class MapperFactura {

    public FacturaDTO toDto(Factura factura) {
        if (factura == null) return null;

        return new FacturaDTO(
                factura.getId(),
                factura.getOrden() != null ? factura.getOrden().getId() : null,
                factura.getNumeroFactura(),
                factura.getNombreComprador(),
                factura.getApellidoComprador(),
                factura.getCuitDni(),
                factura.getNumeroOrden(),
                factura.getDetallesItems(),
                factura.getFechaFactura(),
                factura.getTotalFacturado(),
                factura.getDescripcionImpositiva(),
                factura.getActiva()
        );
    }

    public Factura toEntity(FacturaDTO dto) {
        if (dto == null) return null;

        Factura factura = new Factura();
        factura.setId(dto.getId());
        factura.setNumeroFactura(dto.getNumeroFactura());
        factura.setNombreComprador(dto.getNombreComprador());
        factura.setApellidoComprador(dto.getApellidoComprador());
        factura.setCuitDni(dto.getCuitDni());
        factura.setNumeroOrden(dto.getNumeroOrden());
        factura.setDetallesItems(dto.getDetallesItems());
        factura.setFechaFactura(dto.getFechaFactura());
        factura.setTotalFacturado(dto.getTotalFacturado());
        factura.setUrlPdf(dto.getUrlPdf());
        factura.setDescripcionImpositiva(dto.getDescripcionImpositiva());
        factura.setActiva(dto.getActiva());

        if (dto.getOrdenId() != null) {
            Orden orden = new Orden();
            orden.setId(dto.getOrdenId());
            factura.setOrden(orden);
        }

        return factura;
    }

    public Factura toEntityConOrden(FacturaDTO dto, Orden orden) {
        if (dto == null) return null;

        Factura factura = new Factura();
        factura.setId(dto.getId());
        factura.setNumeroFactura(dto.getNumeroFactura());
        factura.setNombreComprador(dto.getNombreComprador());
        factura.setApellidoComprador(dto.getApellidoComprador());
        factura.setCuitDni(dto.getCuitDni());
        factura.setNumeroOrden(dto.getNumeroOrden());
        factura.setDetallesItems(dto.getDetallesItems());
        factura.setFechaFactura(dto.getFechaFactura());
        factura.setTotalFacturado(dto.getTotalFacturado());
        factura.setUrlPdf(dto.getUrlPdf());
        factura.setDescripcionImpositiva(dto.getDescripcionImpositiva());
        factura.setActiva(dto.getActiva());
        factura.setOrden(orden);

        return factura;
    }
}
