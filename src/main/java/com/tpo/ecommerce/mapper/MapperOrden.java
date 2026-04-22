package com.tpo.ecommerce.mapper;

import com.tpo.ecommerce.dto.ItemOrdenDTO;
import com.tpo.ecommerce.dto.OrdenDTO;
import com.tpo.ecommerce.entity.ItemOrden;
import com.tpo.ecommerce.entity.Orden;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;

@Component
public class MapperOrden {

    public OrdenDTO toDto(Orden orden) {
        List<ItemOrdenDTO> items = orden.getItems().stream()
                .sorted(Comparator.comparing(ItemOrden::getId, Comparator.nullsLast(Long::compareTo)))
                .map(item -> new ItemOrdenDTO(
                        item.getId(),
                        item.getProducto().getId(),
                        item.getProductoTitulo(),
                        item.getPrecioUnitario(),
                        item.getCantidad()
                ))
                .toList();

        OrdenDTO dto = new OrdenDTO();
        dto.setId(orden.getId());
        dto.setCompradorId(orden.getComprador().getId());
        dto.setItems(items);
        dto.setEstado(orden.getEstado());
        dto.setTotal(orden.getTotal());

        if (orden.getDireccion() != null) {
            dto.setDireccionId(orden.getDireccion().getId());
        }
        if (orden.getDescuento() != null) {
            dto.setDescuentoId(orden.getDescuento().getId());
            dto.setDescuentoAplicado(orden.getDescuentoAplicado());
        }

        return dto;
    }
}
