package com.tpo.ecommerce.service;

import com.tpo.ecommerce.dto.ActualizarPagoDTO;
import com.tpo.ecommerce.dto.PagoDTO;
import com.tpo.ecommerce.dto.RealizarPagoDTO;

public interface IPagoService {

    PagoDTO crearPendientePorOrden(Long ordenId);

    PagoDTO obtenerPorId(Long pagoId);

    PagoDTO obtenerPorOrden(Long ordenId);

    PagoDTO pagarOrden(Long ordenId, RealizarPagoDTO dto);

    PagoDTO actualizarPago(Long pagoId, ActualizarPagoDTO dto);
}
