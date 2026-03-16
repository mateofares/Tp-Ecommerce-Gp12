package com.tpo.ecommerce.dto;

import com.tpo.ecommerce.enums.UserRol;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UsuarioDTO {
    private Long id;
    private UserRol userRol;
    private String nombre;
    private String mail;
    private String contrasenia; //quizas sacarla
    private String apellido;
}
