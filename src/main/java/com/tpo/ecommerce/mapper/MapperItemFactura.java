package com.tpo.ecommerce.mapper;

import com.tpo.ecommerce.dto.ItemFacturaDTO;
import com.tpo.ecommerce.entity.ItemFactura;
import com.tpo.ecommerce.entity.Factura;
import org.springframework.stereotype.Component;

@Component
public class MapperItemFactura {

    public ItemFacturaDTO toDto(ItemFactura itemFactura) {
        if (itemFactura == null) return null;

        return new ItemFacturaDTO(
                itemFactura.getId(),
                itemFactura.getFactura() != null ? itemFactura.getFactura().getId() : null,
                itemFactura.getProductoTitulo(),
                itemFactura.getPrecioUnitario(),
                itemFactura.getCantidad()
        );
    }

    public ItemFactura toEntity(ItemFacturaDTO dto) {
        if (dto == null) return null;

        ItemFactura itemFactura = new ItemFactura();
        itemFactura.setId(dto.getId());
        itemFactura.setProductoTitulo(dto.getProductoTitulo());
        itemFactura.setPrecioUnitario(dto.getPrecioUnitario());
        itemFactura.setCantidad(dto.getCantidad());

        if (dto.getFacturaId() != null) {
            Factura factura = new Factura();
            factura.setId(dto.getFacturaId());
            itemFactura.setFactura(factura);
        }

        return itemFactura;
    }

    public ItemFactura toEntityConFactura(ItemFacturaDTO dto, Factura factura) {
        if (dto == null) return null;

        ItemFactura itemFactura = new ItemFactura();
        itemFactura.setId(dto.getId());
        itemFactura.setProductoTitulo(dto.getProductoTitulo());
        itemFactura.setPrecioUnitario(dto.getPrecioUnitario());
        itemFactura.setCantidad(dto.getCantidad());
        itemFactura.setFactura(factura);

        return itemFactura;
    }
}
