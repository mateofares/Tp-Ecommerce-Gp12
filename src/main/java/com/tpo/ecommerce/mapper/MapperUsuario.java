package com.tpo.ecommerce.mapper;

import com.tpo.ecommerce.dto.UsuarioDTO;
import com.tpo.ecommerce.entity.Usuario;
import org.springframework.stereotype.Component;

@Component
public class MapperUsuario {

    public UsuarioDTO toDto(Usuario usuario){
        return new UsuarioDTO(
                usuario.getId(),
                usuario.getUserRol(),
                usuario.getNombre(),
                usuario.getMail(),
                usuario.getContrasenia(),
                usuario.getApellido()
        );
    }

}
