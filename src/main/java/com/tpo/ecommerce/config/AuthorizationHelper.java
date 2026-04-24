package com.tpo.ecommerce.config;

import com.tpo.ecommerce.entity.Usuario;
import com.tpo.ecommerce.repository.UsuarioRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class AuthorizationHelper {

    private final UsuarioRepository usuarioRepository;

    public boolean isAdmin() {
        return getAuthentication().getAuthorities().stream()
                .anyMatch(a -> "ADMINISTRADOR".equals(a.getAuthority()));
    }

    public Long getAuthenticatedUserId() {
        Authentication authentication = getAuthentication();
        Object principal = authentication.getPrincipal();
        if (principal instanceof Usuario usuario) {
            return usuario.getId();
        }
        if (principal instanceof UserDetails userDetails) {
            return usuarioRepository.findByMail(userDetails.getUsername())
                    .map(Usuario::getId)
                    .orElseThrow(() -> new AccessDeniedException("Usuario autenticado no encontrado"));
        }
        throw new AccessDeniedException("No se pudo determinar el usuario autenticado");
    }

    /**
     * Verifica que el usuario autenticado sea el dueño del recurso (ownerId) o un administrador.
     * Lanza AccessDeniedException si no está autorizado.
     */
    public void authorize(Long ownerId) {
        if (ownerId == null) {
            throw new AccessDeniedException("No se pudo determinar el propietario del recurso");
        }
        if (isAdmin()) return;
        Long authUserId = getAuthenticatedUserId();
        if (!authUserId.equals(ownerId)) {
            throw new AccessDeniedException("No tenes permisos para realizar esta accion");
        }
    }

    /**
     * Verifica que el usuario autenticado sea administrador.
     * Lanza AccessDeniedException si no lo es.
     */
    public void requireAdmin() {
        if (!isAdmin()) {
            throw new AccessDeniedException("Esta accion requiere permisos de administrador");
        }
    }

    private Authentication getAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedException("No autenticado");
        }
        return authentication;
    }
}
