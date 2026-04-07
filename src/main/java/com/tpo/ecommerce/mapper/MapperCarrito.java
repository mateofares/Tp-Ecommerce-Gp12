package com.tpo.ecommerce.mapper;

import com.tpo.ecommerce.dto.CarritoDTO;
import com.tpo.ecommerce.dto.ItemCarritoDTO;
import com.tpo.ecommerce.entity.Carrito;
import com.tpo.ecommerce.entity.ItemCarrito;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;

@Component
public class MapperCarrito {

    public CarritoDTO toDto(Carrito carrito) {
        List<ItemCarritoDTO> items = carrito.getItems().stream()
                .sorted(Comparator.comparing(ItemCarrito::getId, Comparator.nullsLast(Long::compareTo)))
                .map(item -> new ItemCarritoDTO(
                        item.getId(),
                        item.getProducto().getId(),
                        item.getProducto().getTitulo(),
                        item.getProducto().getPrecio(),
                        item.getCantidad()
                ))
                .toList();

        double total = items.stream()
                .mapToDouble(item -> (item.getProductoPrecio() != null ? item.getProductoPrecio() : 0D) * item.getCantidad())
                .sum();

        return new CarritoDTO(
                carrito.getId(),
                carrito.getComprador().getId(),
                items,
                total
        );
    }
}
