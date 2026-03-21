package com.tpo.ecommerce.service;

import com.tpo.ecommerce.dto.CarritoDTO;
import com.tpo.ecommerce.dto.OrdenDTO;

public interface ICarritoService {
    
    CarritoDTO agregar(Long compradorId, Long productoId, int cantidad);
    
    CarritoDTO eliminar(Long carritoId, Long productoId);
    
    CarritoDTO vaciar(Long compradorId);
    
    CarritoDTO ver(Long compradorId);
    
    OrdenDTO comprar(Long compradorId);
    
}