package com.tpo.ecommerce.service;

import com.tpo.ecommerce.dto.ItemFacturaDTO;
import java.util.List;

public interface IItemFacturaService {

    List<ItemFacturaDTO> obtenerPorFactura(Long facturaId);

    ItemFacturaDTO obtenerPorId(Long id);
}
