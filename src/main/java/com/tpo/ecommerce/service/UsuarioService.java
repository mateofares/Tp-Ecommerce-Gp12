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

        if(id != null) usuarios = usuarios.stream().filter(usuario -> usuario.getId().equals(id)).toList();

        if (userRol != null )usuarios = usuarios.stream().filter(usuario -> usuario.getUserRol() == userRol).toList();

        if (nombre != null) usuarios = usuarios.stream().filter(usuario -> usuario.getNombre().equalsIgnoreCase(nombre)).toList();

        if (mail != null) usuarios = usuarios.stream().filter(usuario -> usuario.getMail().equalsIgnoreCase(mail)).toList();

        if (apellido != null) usuarios = usuarios.stream().filter(usuario -> usuario.getApellido().equalsIgnoreCase(apellido)).toList();

        return usuarios.stream().map(usuario -> mapperUsuario.toDto(usuario)).toList();
    }

    @Override
    public UsuarioDTO createUsuario(UsuarioDTO usuario) {
        return mapperUsuario.toDto(usuarioRepository.save(
                new Usuario(
                    usuario.getApellido(),
                        usuario.getContrasenia(),
                        usuario.getMail(),
                        usuario.getNombre(),
                        usuario.getUserRol()
                )));
    }

    @Override
    public void deleteUsuario(Long id) {
        usuarioRepository.deleteById(id);
    }
}

