package com.tpo.ecommerce.dto;

import com.tpo.ecommerce.enums.UserRol;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UsuarioDTO {
    private Long id;
    private UserRol userRol;
    private String nombre;
    private String mail;
    private String contrasenia; //quizas sacarla
    private String apellido;

    public UsuarioDTO(String apellido, String contrasenia, String mail, String nombre, UserRol userRol) {
        this.apellido = apellido;
        this.contrasenia = contrasenia;
        this.mail = mail;
        this.nombre = nombre;
        this.userRol = userRol;
    }
}
