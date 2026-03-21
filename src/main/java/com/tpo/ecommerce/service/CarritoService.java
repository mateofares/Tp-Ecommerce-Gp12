package com.tpo.ecommerce.service;

import com.tpo.ecommerce.dto.CarritoDTO;
import com.tpo.ecommerce.dto.ItemCarritoDTO;
import com.tpo.ecommerce.dto.OrdenDTO;
import com.tpo.ecommerce.exceptions.DuplicateResourceException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class CarritoService {

    public CarritoDTO agregar(CarritoDTO dto) {
        validarProductosUnicos(dto.getItems());
        return dto;
    }

    public CarritoDTO eliminar(CarritoDTO dto) {

        return dto;
    }

    public CarritoDTO vaciar(CarritoDTO dto) {

        return dto;
    }

    public CarritoDTO ver(Long compradorId) {

        return null;
    }

    public OrdenDTO comprar(CarritoDTO dto) {

        return null;
    }

    private void validarProductosUnicos(List<ItemCarritoDTO> items) {
        if (items == null || items.isEmpty()) {
            return;
        }
        Set<Long> vistos = new HashSet<>();
        for (ItemCarritoDTO item : items) {
            Long productoId = item.getProductoId();
            if (productoId == null) {
                continue;
            }
            if (!vistos.add(productoId)) {
                throw new DuplicateResourceException("Producto", "id", productoId);
            }
        }
    }
}
