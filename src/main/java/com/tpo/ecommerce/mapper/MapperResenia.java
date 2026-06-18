package com.tpo.ecommerce.mapper;

import com.tpo.ecommerce.dto.ReseniaDTO;
import com.tpo.ecommerce.entity.Resenia;
import org.springframework.stereotype.Component;

@Component
public class MapperResenia {

    public ReseniaDTO toDto(Resenia resenia) {
        if (resenia == null) {
            return null;
        }
        String compradorNombre = resenia.getComprador() != null
                ? resenia.getComprador().getNombre() + " " + resenia.getComprador().getApellido()
                : null;
        String productoTitulo = resenia.getProducto() != null ? resenia.getProducto().getTitulo() : null;
        return new ReseniaDTO(
                resenia.getId(),
                resenia.getProducto() != null ? resenia.getProducto().getId() : null,
                resenia.getComprador() != null ? resenia.getComprador().getId() : null,
                resenia.getVendedor() != null ? resenia.getVendedor().getId() : null,
                resenia.getOrden() != null ? resenia.getOrden().getId() : null,
                resenia.getCalificacion(),
                resenia.getComentarios(),
                resenia.getFecha(),
                resenia.getVerificado(),
                compradorNombre,
                productoTitulo
        );
    }
}
