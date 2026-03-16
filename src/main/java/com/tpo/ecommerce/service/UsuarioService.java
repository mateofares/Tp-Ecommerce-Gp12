package com.tpo.ecommerce.service;

import com.tpo.ecommerce.dto.UsuarioDTO;
import com.tpo.ecommerce.entity.Usuario;
import com.tpo.ecommerce.enums.UserRol;
import lombok.AllArgsConstructor;
import com.tpo.ecommerce.mapper.MapperUsuario;
import org.springframework.stereotype.Service;
import com.tpo.ecommerce.repository.UsuarioRepository;

import java.util.List;

@AllArgsConstructor
@Service
public class UsuarioService implements IUsuarioService{
    private UsuarioRepository usuarioRepository;
    private MapperUsuario mapperUsuario;

    @Override
    public List<UsuarioDTO> getUsuarios(Long id,UserRol userRol, String nombre, String mail, String apellido) {
        List<Usuario> usuarios = usuarioRepository.findAll();

        usuarios = usuarios.stream().filter(usuario -> usuario.getId().equals(id)).toList();

        usuarios = usuarios.stream().filter(usuario -> usuario.getUserRol()==userRol).toList();

        usuarios = usuarios.stream().filter(usuario -> usuario.getNombre().equals(nombre)).toList();

        usuarios = usuarios.stream().filter(usuario -> usuario.getMail().equals(mail)).toList();

        usuarios = usuarios.stream().filter(usuario -> usuario.getApellido().equals(apellido)).toList();

        return usuarios.stream().map(usuario -> mapperUsuario.toDto(usuario)).toList();
    }
}




