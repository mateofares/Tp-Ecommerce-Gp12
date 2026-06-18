package com.tpo.ecommerce.service;

import com.tpo.ecommerce.dto.FacturaDTO;

import java.util.List;

public interface IFacturaService {

    FacturaDTO crearFacturaAutomatica(Long ordenId);

    FacturaDTO obtenerPorId(Long id);

    FacturaDTO obtenerPorOrden(Long ordenId);

    List<FacturaDTO> listarTodas();

    String descargarPDF(Long id);

    void anularFactura(Long id);
}
