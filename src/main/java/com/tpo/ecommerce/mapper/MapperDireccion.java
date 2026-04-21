package com.tpo.ecommerce.mapper;
import com.tpo.ecommerce.dto.DireccionDTO;
import com.tpo.ecommerce.entity.Direccion;
import com.tpo.ecommerce.entity.Usuario;
import org.springframework.stereotype.Component;


@Component
public class MapperDireccion {

    public DireccionDTO toDTO(Direccion direccion) {
        if (direccion == null) {
            return null;
        }
        return new DireccionDTO(
                direccion.getCalle(),
                direccion.getNumero(),
                direccion.getCiudad(),
                direccion.getProvincia(),
                direccion.getCodigoPostal(),
                direccion.getPais()
        );
    }

    public Direccion toEntity(DireccionDTO dto) {
        if (dto == null) {
            return null;
        }
        Direccion direccion = new Direccion();
        direccion.setCalle(dto.getCalle());
        direccion.setNumero(dto.getNumero());
        direccion.setCiudad(dto.getCiudad());
        direccion.setProvincia(dto.getProvincia());
        direccion.setCodigoPostal(dto.getCodigoPostal());
        direccion.setPais(dto.getPais());
        
        return direccion;
    }
}