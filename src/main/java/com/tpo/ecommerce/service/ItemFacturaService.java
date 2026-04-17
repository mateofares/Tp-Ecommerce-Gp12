package com.tpo.ecommerce.service;

import com.tpo.ecommerce.dto.ItemFacturaDTO;
import com.tpo.ecommerce.entity.ItemFactura;
import com.tpo.ecommerce.exceptions.NotFoundException;
import com.tpo.ecommerce.mapper.MapperItemFactura;
import com.tpo.ecommerce.repository.ItemFacturaRepository;
import com.tpo.ecommerce.repository.FacturaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class ItemFacturaService implements IItemFacturaService {

    private final ItemFacturaRepository itemFacturaRepository;
    private final FacturaRepository facturaRepository;
    private final MapperItemFactura mapperItemFactura;

    @Override
    public List<ItemFacturaDTO> obtenerPorFactura(Long facturaId) {
        if (!facturaRepository.existsById(facturaId)) {
            throw new NotFoundException("Factura no encontrada con id: " + facturaId);
        }

        List<ItemFactura> items = itemFacturaRepository.findByFacturaId(facturaId);
        return items.stream()
                .map(mapperItemFactura::toDto)
                .toList();
    }

    @Override
    public ItemFacturaDTO obtenerPorId(Long id) {
        ItemFactura item = itemFacturaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Item factura no encontrado con id: " + id));
        return mapperItemFactura.toDto(item);
    }
}
