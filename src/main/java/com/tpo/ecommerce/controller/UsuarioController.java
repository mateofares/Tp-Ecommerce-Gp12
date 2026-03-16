package com.tpo.ecommerce.controller;

import com.tpo.ecommerce.dto.UsuarioDTO;
import com.tpo.ecommerce.enums.UserRol;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.tpo.ecommerce.service.UsuarioService;

import java.util.List;


@RestController
@RequestMapping("/usuario")
//luego agregar cross (cuando este el front)
public class UsuarioController {
    private UsuarioService usuarioService;

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


}
