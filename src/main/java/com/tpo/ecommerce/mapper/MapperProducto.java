package com.tpo.ecommerce.mapper;

import com.tpo.ecommerce.dto.ProductoDTO;
import com.tpo.ecommerce.entity.Producto;
import com.tpo.ecommerce.entity.Usuario;
import org.springframework.stereotype.Component;

@Component
public class MapperProducto {

    public ProductoDTO toDto(Producto producto) {
        if (producto == null) return null;
        return new ProductoDTO(
                producto.getId(),
                producto.getUsuario() != null ? producto.getUsuario().getId() : null,
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
        Producto producto = new Producto();
        producto.setId(dto.getId());
        producto.setTitulo(dto.getTitulo());
        producto.setDescripcion(dto.getDescripcion());
        producto.setPrecio(dto.getPrecio());
        producto.setCategoria(dto.getCategoria());
        producto.setMarca(dto.getMarca());
        producto.setTalle(dto.getTalle());
        producto.setColor(dto.getColor());
        producto.setEstado(dto.getEstado());
        producto.setImagenUrl(dto.getImagenUrl());
        if (dto.getUsuarioId() != null) {
            Usuario usuario = new Usuario();
            usuario.setId(dto.getUsuarioId());
            producto.setUsuario(usuario);
        }
        return producto;
    }
}
