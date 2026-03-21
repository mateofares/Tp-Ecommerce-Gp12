package com.tpo.ecommerce.entity;

import com.tpo.ecommerce.enums.UserRol;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "usuario")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private UserRol userRol;
    private String nombre;
    private String mail;
    private String contrasenia;
    private String apellido;

    public Usuario(String apellido, String contrasenia, String mail, String nombre, UserRol userRol) {
        this.apellido = apellido;
        this.contrasenia = contrasenia;
        this.mail = mail;
        this.nombre = nombre;
        this.userRol = userRol;
    }
}
