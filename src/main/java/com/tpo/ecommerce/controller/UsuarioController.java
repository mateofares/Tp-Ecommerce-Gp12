package com.tpo.ecommerce.controller;

import com.tpo.ecommerce.dto.LoginRequestDTO;
import com.tpo.ecommerce.dto.UsuarioDTO;
import com.tpo.ecommerce.enums.UserRol;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.tpo.ecommerce.service.UsuarioService;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
//luego agregar cross (cuando este el front)
@AllArgsConstructor
public class UsuarioController {
    private final UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<List<UsuarioDTO>> getUsuarios(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) UserRol userRol,
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String mail,
            @RequestParam(required = false) String apellido
    ){
        return ResponseEntity.ok(usuarioService.getUsuarios(id,userRol,nombre,mail,apellido));
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteUsuario(@RequestParam Long id) {
        usuarioService.deleteUsuario(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UsuarioDTO> updateUsuario(
            @PathVariable Long id,
            @RequestBody UsuarioDTO usuario
    ){
        return ResponseEntity.ok(
                usuarioService.updateUsuario(
                        id,
                        usuario.getUserRol(),
                        usuario.getNombre(),
                        usuario.getMail(),
                        usuario.getApellido(),
                        usuario.getContrasenia()
                )
        );
    }

    @PostMapping("/register")
    public ResponseEntity<UsuarioDTO> register(@RequestBody UsuarioDTO usuario) {
        return ResponseEntity.ok(usuarioService.register(usuario));
    }

    @PostMapping("/login")
    public ResponseEntity<UsuarioDTO> login(@RequestBody LoginRequestDTO loginRequest) {
        return ResponseEntity.ok(usuarioService.login(loginRequest));
    }

}
