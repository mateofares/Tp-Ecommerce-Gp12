package com.tpo.ecommerce.service;

import java.util.List;

import com.tpo.ecommerce.dto.OrdenDTO;

public interface IOrdenService {
    OrdenDTO comprar(OrdenDTO ordenDTO);
    List<OrdenDTO> misCompras(Long compradorId);
    List<OrdenDTO> misVentas(Long vendedorId);
    OrdenDTO aplicarDescuentoAOrden(Long ordenId, Long descuentoId);
    OrdenDTO removerDescuentoDeOrden(Long ordenId);
}