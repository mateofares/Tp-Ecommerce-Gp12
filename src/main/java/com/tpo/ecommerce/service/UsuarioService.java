package com.tpo.ecommerce.service;

import com.tpo.ecommerce.dto.LoginRequestDTO;
import com.tpo.ecommerce.dto.UsuarioDTO;
import com.tpo.ecommerce.entity.Usuario;
import com.tpo.ecommerce.enums.UserRol;
import lombok.AllArgsConstructor;
import com.tpo.ecommerce.mapper.MapperUsuario;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;
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

        if (userRol != null )usuarios = usuarios.stream().filter(usuario -> usuario.getUserRol().toString().equalsIgnoreCase(userRol.toString())).toList();

        if (nombre != null) usuarios = usuarios.stream().filter(usuario -> usuario.getNombre().equalsIgnoreCase(nombre)).toList();

        if (mail != null) usuarios = usuarios.stream().filter(usuario -> usuario.getMail().equalsIgnoreCase(mail)).toList();

        if (apellido != null) usuarios = usuarios.stream().filter(usuario -> usuario.getApellido().equalsIgnoreCase(apellido)).toList();

        return usuarios.stream().map(usuario -> mapperUsuario.toDto(usuario)).toList();
    }

    private UsuarioDTO createUsuario(UsuarioDTO usuario) {
        validarData(
                usuario.getNombre(),
                usuario.getMail(),
                usuario.getContrasenia(),
                usuario.getApellido()
        );

        validarMailFormato(usuario.getMail());
        mailDisponible(usuario.getMail(), null);

        return mapperUsuario.toDto(usuarioRepository.save(
                new Usuario(
                        usuario.getApellido().trim(),
                        usuario.getContrasenia(),
                        usuario.getMail().trim(),
                        usuario.getNombre().trim(),
                        usuario.getUserRol()
                )));
    }

    @Override
    public UsuarioDTO register(UsuarioDTO usuario) {
        return createUsuario(usuario);
    }

    @Override
    public UsuarioDTO login(LoginRequestDTO loginRequest) {
        Usuario usuario = usuarioRepository.findByMail(loginRequest.getMail())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!usuario.getContrasenia().equals(loginRequest.getContrasenia())) {
            throw new RuntimeException("Contrasenia incorrecta");
        }

        return mapperUsuario.toDto(usuario);
    }

    @Override
    public void deleteUsuario(Long id) {
        usuarioRepository.deleteById(id);
    }

    public UsuarioDTO updateUsuario(Long id,UserRol userRol, String nombre, String mail, String apellido,String contrasenia){
        if (usuarioRepository.existsById(id)){
            Usuario usuario = usuarioRepository.findById(id).get();


            if (userRol != null )usuario.setUserRol(userRol);

            if (nombre != null) usuario.setNombre(nombre);

            if (mail != null) {
                validarMailFormato(mail);
                mailDisponible(mail, id);
                usuario.setMail(mail.trim());
            }

            if (apellido != null) usuario.setApellido(apellido);

            if (contrasenia != null) usuario.setContrasenia(contrasenia);

            return mapperUsuario.toDto(usuarioRepository.save(usuario));

        }else {
            throw new RuntimeException("Usuario no encontrado por id");
        }


    }

    private void validarData(String nombre, String mail, String contrasenia, String apellido) {

        if (!StringUtils.hasText(nombre) || !StringUtils.hasText(mail)
                || !StringUtils.hasText(contrasenia) || !StringUtils.hasText(apellido)) {
            throw new RuntimeException("Todos los campos son obligatorios");
        }
    }

    private void validarMailFormato(String mail) {
        if (!StringUtils.hasText(mail)) {
            throw new RuntimeException("Mail invalido");
        }

        String mailTrim = mail.trim();
        if (!mailTrim.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            throw new RuntimeException("Formato de mail invalido");
        }
    }

    private void mailDisponible(String mail, Long idUsuarioActual) {
        usuarioRepository.findByMail(mail).ifPresent(usuarioExistente -> {
            if (idUsuarioActual == null || !usuarioExistente.getId().equals(idUsuarioActual)) {
                throw new RuntimeException("Ya existe un usuario registrado con ese mail");
            }
        });
    }

}
