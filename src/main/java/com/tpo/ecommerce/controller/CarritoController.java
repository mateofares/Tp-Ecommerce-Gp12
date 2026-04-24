package com.tpo.ecommerce.controller;

import com.tpo.ecommerce.dto.CarritoDTO;
import com.tpo.ecommerce.dto.OrdenDTO;
import com.tpo.ecommerce.entity.Usuario;
import com.tpo.ecommerce.repository.UsuarioRepository;
import com.tpo.ecommerce.service.ICarritoService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/carrito")
public class CarritoController {
    
    private final ICarritoService carritoService;
    private final UsuarioRepository usuarioRepository;

    @PostMapping("/agregar")
    public CarritoDTO agregar(@RequestBody CarritoDTO dto) {
        authorizeRequester(dto.getCompradorId());
        return carritoService.agregar(dto);
    }
    
    @DeleteMapping("/eliminar")
    public CarritoDTO eliminar(@RequestBody CarritoDTO dto) {
        authorizeRequester(dto.getCompradorId());
        return carritoService.eliminar(dto);
    }
    
    @DeleteMapping("/vaciar")
    public CarritoDTO vaciar(@RequestBody CarritoDTO dto) {
        authorizeRequester(dto.getCompradorId());
        return carritoService.vaciar(dto);
    }
    
    @GetMapping("/{compradorId}")
    public CarritoDTO ver(@PathVariable Long compradorId) {
        authorizeRequester(compradorId);
        return carritoService.ver(compradorId);
    }
    
    @PostMapping("/comprar")
    public OrdenDTO comprar(@RequestBody CarritoDTO dto) {
        authorizeRequester(dto.getCompradorId());
        return carritoService.comprar(dto);
    }

    @PostMapping("/{compradorId}/descuento/{descuentoId}")
    public CarritoDTO aplicarDescuento(
            @PathVariable Long compradorId,
            @PathVariable Long descuentoId
    ) {
        authorizeRequester(compradorId);
        return carritoService.aplicarDescuentoAlCarrito(compradorId, descuentoId);
    }

    @DeleteMapping("/{compradorId}/descuento")
    public CarritoDTO removerDescuento(@PathVariable Long compradorId) {
        authorizeRequester(compradorId);
        return carritoService.removerDescuentoDelCarrito(compradorId);
    }

    private void authorizeRequester(Long compradorId) {
        if (compradorId == null) {
            throw new AccessDeniedException("Debe indicar el compradorId");
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedException("No autenticado");
        }

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> "ADMINISTRADOR".equals(a.getAuthority()));
        if (isAdmin) {
            return; // admins pueden acceder a cualquier carrito
        }

        Object principal = authentication.getPrincipal();
        Long authUserId = null;
        if (principal instanceof Usuario) {
            authUserId = ((Usuario) principal).getId();
        } else if (principal instanceof UserDetails) {
            String mail = ((UserDetails) principal).getUsername();
            authUserId = usuarioRepository.findByMail(mail).map(Usuario::getId).orElse(null);
        }

        if (authUserId == null || !authUserId.equals(compradorId)) {
            throw new AccessDeniedException("No tenes permisos para realizar esta accion");
        }
    }
    
}