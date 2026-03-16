package com.tpo.ecommerce.entity;

import com.tpo.ecommerce.enums.UserRol;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "usuario")
@Getter
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

}
