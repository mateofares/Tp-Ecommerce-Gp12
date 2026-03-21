package com.tpo.ecommerce.mapper;

import com.tpo.ecommerce.entity.Producto;
import org.springframework.stereotype.Component;

@Component
public class MapperProducto {

    public ProductoDTO toDto(Producto producto){
        return new ProductoDTO(
                producto.getId(),
                producto.getNombre(),
                producto.getDescripcion(),
                producto.getPrecio(),
                producto.getStock(),
                producto.getTalle(),
                producto.getColor(),
                producto.getMarca(),
                producto.getCategoria(),
                producto.getVendedor()
        );
    }

}