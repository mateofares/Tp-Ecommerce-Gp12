package com.tpo.ecommerce.service;

import com.tpo.ecommerce.dto.ItemOrdenDTO;
import com.tpo.ecommerce.dto.OrdenDTO;
import com.tpo.ecommerce.exceptions.DuplicateResourceException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class OrdenService {

    public OrdenDTO comprar(OrdenDTO dto) {
        validarProductosUnicos(dto.getItems());
        return dto;
    }

    public List<OrdenDTO> misCompras(Long compradorId) {

        return List.of();
    }

    public List<OrdenDTO> misVentas(Long vendedorId) {

        return List.of();
    }

    private void validarProductosUnicos(List<ItemOrdenDTO> items) {
        if (items == null || items.isEmpty()) {
            return;
        }
        Set<Long> vistos = new HashSet<>();
        for (ItemOrdenDTO item : items) {
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
