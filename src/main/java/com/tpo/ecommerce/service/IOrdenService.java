package com.tpo.ecommerce.service;

import java.util.List;

import com.tpo.ecommerce.dto.OrdenDTO;

public interface IOrdenService {
    public OrdenDTO comprar(Long compradorId, Long productoId);
    public List<OrdenDTO> misCompras(Long compradorId); 
    public List<OrdenDTO> misVentas(Long vendedorId);
}