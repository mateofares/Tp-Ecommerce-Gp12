package com.tpo.ecommerce.dto;

import com.tpo.ecommerce.enums.UserRol;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    private String nombre;
    private String apellido;
    private String mail;
    private String contrasenia;
    private UserRol userRol;
}
