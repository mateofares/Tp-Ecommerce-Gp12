package com.tpo.ecommerce.mapper;

import com.tpo.ecommerce.dto.ProductoDTO;
import com.tpo.ecommerce.entity.Descuento;
import com.tpo.ecommerce.entity.Producto;
import com.tpo.ecommerce.entity.Usuario;
import com.tpo.ecommerce.enums.TipoDescuento;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class MapperProducto {

    public ProductoDTO toDto(Producto producto) {
        if (producto == null) return null;

        ProductoDTO dto = new ProductoDTO();
        dto.setId(producto.getId());
        dto.setUsuarioId(producto.getUsuario() != null ? producto.getUsuario().getId() : null);
        dto.setTitulo(producto.getTitulo());
        dto.setDescripcion(producto.getDescripcion());
        dto.setPrecio(producto.getPrecio());
        dto.setCategoria(producto.getCategoria());
        dto.setMarca(producto.getMarca());
        dto.setTalle(producto.getTalle());
        dto.setColor(producto.getColor());
        dto.setEstado(producto.getEstado());
        dto.setImagenUrl(producto.getImagenUrl());

        Descuento descuento = producto.getDescuento();
        if (descuento != null) {
            dto.setDescuentoId(descuento.getId());
            dto.setPrecioConDescuento(calcularPrecioEfectivo(producto.getPrecio(), descuento));
        }

        return dto;
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

    /**
     * Devuelve el precio tras aplicar el descuento si está vigente hoy.
     * Retorna el precio original si el descuento expiró o aún no comenzó.
     */
    public static Double calcularPrecioEfectivo(Double precio, Descuento descuento) {
        if (precio == null || descuento == null) return precio;
        LocalDate hoy = LocalDate.now();
        if (hoy.isBefore(descuento.getValidoDesde()) || hoy.isAfter(descuento.getValidoHasta())) {
            return precio;
        }
        if (descuento.getTipo() == TipoDescuento.PORCENTAJE) {
            return precio * (1 - descuento.getValor() / 100.0);
        }
        return Math.max(0, precio - descuento.getValor());
    }
}
