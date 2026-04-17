package com.tpo.ecommerce.service;

import com.tpo.ecommerce.dto.FacturaDTO;

public interface IFacturaService {

    FacturaDTO crearFacturaAutomatica(Long ordenId);

    FacturaDTO obtenerPorId(Long id);

    FacturaDTO obtenerPorOrden(Long ordenId);

    String descargarPDF(Long id);

    void anularFactura(Long id);
}
