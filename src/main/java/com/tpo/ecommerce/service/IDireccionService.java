package com.tpo.ecommerce.service;
import com.tpo.ecommerce.dto.DireccionDTO;
import java.util.List;


public interface IDireccionService {

    DireccionDTO crear(DireccionDTO dto);

    DireccionDTO obtener(Long id);

    List<DireccionDTO> obtenerPorUsuario(Long usuarioId);

    DireccionDTO actualizar(Long id, DireccionDTO dto);

    void eliminarLogico(Long id);

    DireccionDTO establecerPredeterminada(Long id);
}