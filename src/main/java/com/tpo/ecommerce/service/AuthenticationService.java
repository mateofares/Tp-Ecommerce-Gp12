package com.tpo.ecommerce.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.tpo.ecommerce.config.JwtService;
import com.tpo.ecommerce.dto.AuthenticationRequest;
import com.tpo.ecommerce.dto.AuthenticationResponse;
import com.tpo.ecommerce.dto.RegisterRequest;
import com.tpo.ecommerce.entity.Usuario;
import com.tpo.ecommerce.enums.EstadoRegistro;
import com.tpo.ecommerce.exceptions.BadRequestException;
import com.tpo.ecommerce.exceptions.DuplicateResourceException;
import com.tpo.ecommerce.exceptions.NotFoundException;
import com.tpo.ecommerce.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        validarData(request.getNombre(), request.getMail(), request.getContrasenia(), request.getApellido());
        validarMailFormato(request.getMail());
        mailDisponible(request.getMail());

        var user = Usuario.builder()
                .nombre(request.getNombre().trim())
                .apellido(request.getApellido().trim())
                .mail(request.getMail().trim())
                .contrasenia(passwordEncoder.encode(request.getContrasenia()))
                .userRol(request.getUserRol())
                .build();

        repository.save(user);

        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getMail(),
                        request.getContrasenia()
                )
        );
        
        var user = repository.findByMail(request.getMail())
                .filter(this::estaActivo)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));
        
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .build();
    }


    private void validarData(String nombre, String mail, String contrasenia, String apellido) {
        if (!StringUtils.hasText(nombre) || !StringUtils.hasText(mail)
                || !StringUtils.hasText(contrasenia) || !StringUtils.hasText(apellido)) {
            throw new BadRequestException("Todos los campos son obligatorios");
        }
    }

    private void validarMailFormato(String mail) {
        String mailTrim = mail.trim();
        if (!mailTrim.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            throw new BadRequestException("Formato de mail invalido");
        }
    }

    private void mailDisponible(String mail) {
        if (repository.findByMail(mail).filter(this::estaActivo).isPresent()) {
            throw new DuplicateResourceException("Usuario", "mail", mail);
        }
    }

    private boolean estaActivo(Usuario usuario) {
        return usuario.getEstadoRegistro() == null || usuario.getEstadoRegistro() == EstadoRegistro.ACTIVO;
    }
}
