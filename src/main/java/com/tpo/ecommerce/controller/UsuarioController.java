package com.tpo.ecommerce.controller;

import com.tpo.ecommerce.dto.UsuarioDTO;
import com.tpo.ecommerce.entity.Usuario;
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

    @GetMapping("/get")
    public ResponseEntity<List<UsuarioDTO>> getUsuarios(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) UserRol userRol,
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String mail,
            @RequestParam(required = false) String apellido
    ){
        return ResponseEntity.ok(usuarioService.getUsuarios(id,userRol,nombre,mail,apellido));
    }

    @PostMapping
    public ResponseEntity<UsuarioDTO> createUsuario(@RequestBody UsuarioDTO usuario) {
        return ResponseEntity.ok(usuarioService.createUsuario(usuario));
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteUsuario(@RequestParam Long id) {
        usuarioService.deleteUsuario(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping
    public ResponseEntity<UsuarioDTO> updateUsuario(
            @RequestParam Long id,
            @RequestParam(required = false) UserRol userRol,
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String mail,
            @RequestParam(required = false) String apellido,
            @RequestParam(required = false) String contrasenia

    ){
        return ResponseEntity.ok(usuarioService.updateUsuario(id,userRol,nombre,mail,apellido,contrasenia));
    }

}
