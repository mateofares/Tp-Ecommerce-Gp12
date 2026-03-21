package com.tpo.ecommerce.mapper;

import com.tpo.ecommerce.dto.ProductoDTO;
import com.tpo.ecommerce.entity.Producto;
import org.springframework.stereotype.Component;

@Component
public class MapperProducto {

    public ProductoDTO toDto(Producto producto) {
        if (producto == null) return null;
        return new ProductoDTO(
                producto.getId(),
                producto.getTitulo(),
                producto.getDescripcion(),
                producto.getPrecio(),
                producto.getCategoria(),
                producto.getMarca(),
                producto.getTalle(),
                producto.getColor(),
                producto.getEstado(),
                producto.getImagenUrl()
        );
    }

    public Producto toEntity(ProductoDTO dto) {
        if (dto == null) return null;
        return new Producto(
                dto.getId(),
                dto.getTitulo(),
                dto.getDescripcion(),
                dto.getPrecio(),
                dto.getCategoria(),
                dto.getMarca(),
                dto.getTalle(),
                dto.getColor(),
                dto.getEstado(),
                dto.getImagenUrl()
        );
    }
}
