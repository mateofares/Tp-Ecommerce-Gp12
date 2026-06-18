package com.tpo.ecommerce.service;

import com.tpo.ecommerce.dto.ReseniaDTO;

import java.util.List;

public interface IReseniaService {
    ReseniaDTO crear(ReseniaDTO dto);
    List<ReseniaDTO> getByProducto(Long productoId);
    List<ReseniaDTO> getByVendedor(Long vendedorId);
}
