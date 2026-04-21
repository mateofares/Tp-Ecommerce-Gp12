package com.tpo.ecommerce.mapper;
import com.tpo.ecommerce.dto.DireccionDTO;
import com.tpo.ecommerce.entity.Direccion;
import com.tpo.ecommerce.entity.Usuario;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class MapperDireccion {

    public DireccionDTO toDto(Direccion direccion) {
        if (direccion == null) {
            return null;
        }
        return new DireccionDTO(
                direccion.getId(),
                direccion.getUsuario() != null ? direccion.getUsuario().getId() : null,
                direccion.getCalle(),
                direccion.getNumero(),
                direccion.getCiudad(),
                direccion.getCodigoPostal(),
                direccion.getProvincia(),
                direccion.getTipoDireccion(),
                direccion.getPredeterminada(),
                direccion.getNotas(),
                direccion.getActiva(),
                direccion.getFechaCreacion()
        );
    }

    public Direccion toEntity(DireccionDTO dto) {
        if (dto == null) {
            return null;
        }
        Direccion direccion = new Direccion();
        direccion.setId(dto.getId());

        if (dto.getUsuarioId() != null) {
            Usuario usuario = new Usuario();
            usuario.setId(dto.getUsuarioId());
            direccion.setUsuario(usuario);
        }

        direccion.setCalle(dto.getCalle());
        direccion.setNumero(dto.getNumero());
        direccion.setCiudad(dto.getCiudad());
        direccion.setCodigoPostal(dto.getCodigoPostal());
        direccion.setProvincia(dto.getProvincia());
        direccion.setTipoDireccion(dto.getTipoDireccion());
        direccion.setPredeterminada(dto.getPredeterminada() != null ? dto.getPredeterminada() : false);
        direccion.setNotas(dto.getNotas());
        direccion.setActiva(dto.getActiva() != null ? dto.getActiva() : true);
        direccion.setFechaCreacion(dto.getFechaCreacion() != null ? dto.getFechaCreacion() : LocalDateTime.now());

        return direccion;
    }
}
