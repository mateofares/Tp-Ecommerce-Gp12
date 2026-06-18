package com.tpo.ecommerce.service;

import com.tpo.ecommerce.dto.DescuentoDTO;

import java.util.List;

public interface IDescuentoService {
    List<DescuentoDTO> getDescuentos(Long id, String codigoDescuento, String tipo);
    DescuentoDTO createDescuento(DescuentoDTO descuentoDTO);
    DescuentoDTO updateDescuento(Long id, DescuentoDTO descuentoDTO);
    void deleteDescuento(Long id);
    void eliminarLogico(Long id);
    DescuentoDTO getDescuentoPorCodigo(String codigoDescuento);
    Double calcularDescuento(Double monto, Long descuentoId);
    Double calcularPrecioFinal(Double monto, Long descuentoId);
    boolean validarDescuentoAplicable(Long descuentoId);
}
