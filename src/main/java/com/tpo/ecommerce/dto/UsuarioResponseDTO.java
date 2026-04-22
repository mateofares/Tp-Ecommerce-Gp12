package com.tpo.ecommerce.dto;

import com.tpo.ecommerce.enums.UserRol;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UsuarioResponseDTO {
    private Long id;
    private UserRol userRol;
    private String nombre;
    private String mail;
    private String apellido;
}
