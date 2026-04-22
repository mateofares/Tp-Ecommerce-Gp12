package com.tpo.ecommerce.service;

import com.tpo.ecommerce.dto.UsuarioResponseDTO;
import com.tpo.ecommerce.entity.Usuario;
import com.tpo.ecommerce.enums.EstadoRegistro;
import com.tpo.ecommerce.enums.UserRol;
import com.tpo.ecommerce.exceptions.BadRequestException;
import com.tpo.ecommerce.exceptions.DuplicateResourceException;
import com.tpo.ecommerce.exceptions.NotFoundException;
import lombok.AllArgsConstructor;
import com.tpo.ecommerce.mapper.MapperUsuario;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import com.tpo.ecommerce.repository.UsuarioRepository;

import java.util.List;

@AllArgsConstructor
@Service
public class UsuarioService implements IUsuarioService {
    
    private final UsuarioRepository usuarioRepository;
    private final MapperUsuario mapperUsuario;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<UsuarioResponseDTO> getUsuarios(Long id, UserRol userRol, String nombre, String mail, String apellido) {
        List<Usuario> usuarios = usuarioRepository.findAll();
        usuarios = usuarios.stream().filter(this::estaActivo).toList();

        if(id != null) usuarios = usuarios.stream().filter(usuario -> usuario.getId().equals(id)).toList();
        if (userRol != null )usuarios = usuarios.stream().filter(usuario -> usuario.getUserRol() == userRol).toList();
        if (nombre != null) usuarios = usuarios.stream().filter(usuario -> usuario.getNombre().equalsIgnoreCase(nombre)).toList();
        if (mail != null) usuarios = usuarios.stream().filter(usuario -> usuario.getMail().equalsIgnoreCase(mail)).toList();
        if (apellido != null) usuarios = usuarios.stream().filter(usuario -> usuario.getApellido().equalsIgnoreCase(apellido)).toList();

        return usuarios.stream().map(mapperUsuario::toResponseDto).toList();
    }

    @Override
    public void deleteUsuario(Long id) {
        if(!usuarioRepository.existsById(id)) {
            throw new NotFoundException("Usuario no encontrado por id");
        }
        usuarioRepository.deleteById(id);
    }

    @Override
    public void eliminarLogico(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado por id"));
        usuario.setEstadoRegistro(EstadoRegistro.ELIMINADO);
        usuarioRepository.save(usuario);
    }

    @Override
    public UsuarioResponseDTO updateUsuario(Long id, UserRol userRol, String nombre, String mail, String apellido, String contrasenia) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado por id"));
        if (!estaActivo(usuario)) {
            throw new NotFoundException("Usuario no encontrado por id");
        }

        if (userRol != null) usuario.setUserRol(userRol);
        if (nombre != null) usuario.setNombre(nombre);
        if (apellido != null) usuario.setApellido(apellido);
        
        if (mail != null && !mail.equals(usuario.getMail())) {
            validarMailFormato(mail);
            mailDisponibleParaUpdate(mail, id);
            usuario.setMail(mail.trim());
        }

        if (StringUtils.hasText(contrasenia)) {
            usuario.setContrasenia(passwordEncoder.encode(contrasenia));
        }

        return mapperUsuario.toResponseDto(usuarioRepository.save(usuario));
    }

    // --- VALIDACIONES SOLO PARA UPDATE ---

    private void validarMailFormato(String mail) {
        String mailTrim = mail.trim();
        if (!mailTrim.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            throw new BadRequestException("Formato de mail invalido");
        }
    }

    private void mailDisponibleParaUpdate(String mail, Long idUsuarioActual) {
        usuarioRepository.findByMail(mail).ifPresent(usuarioExistente -> {
            if (!estaActivo(usuarioExistente)) {
                return;
            }
            if (!usuarioExistente.getId().equals(idUsuarioActual)) {
                throw new DuplicateResourceException("Usuario", "mail", mail);
            }
        });
    }

    private boolean estaActivo(Usuario usuario) {
        return usuario.getEstadoRegistro() == null || usuario.getEstadoRegistro() == EstadoRegistro.ACTIVO;
    }
}
