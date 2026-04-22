package com.tpo.ecommerce.service;

import com.tpo.ecommerce.dto.CarritoDTO;
import com.tpo.ecommerce.dto.OrdenDTO;

public interface ICarritoService {

    CarritoDTO agregar(CarritoDTO dto);

    CarritoDTO eliminar(CarritoDTO dto);

    CarritoDTO vaciar(CarritoDTO dto);

    CarritoDTO ver(Long compradorId);

    OrdenDTO comprar(CarritoDTO dto);
    CarritoDTO aplicarDescuentoAlCarrito(Long compradorId, Long descuentoId);
    CarritoDTO removerDescuentoDelCarrito(Long compradorId);
}
