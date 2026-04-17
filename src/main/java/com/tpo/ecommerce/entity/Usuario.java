package com.tpo.ecommerce.entity;

import com.tpo.ecommerce.enums.UserRol;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "usuario")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_rol")
    private UserRol userRol;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "mail")
    private String mail;

    @Column(name = "contrasenia")
    private String contrasenia;

    @Column(name = "apellido")
    private String apellido;

    @OneToMany(mappedBy = "usuario")
    private List<Producto> productos;

    @OneToMany(mappedBy = "usuario")
    private List<Direccion> direcciones;


    public Usuario(String apellido, String contrasenia, String mail, String nombre, UserRol userRol) {
        this.apellido = apellido;
        this.contrasenia = contrasenia;
        this.mail = mail;
        this.nombre = nombre;
        this.userRol = userRol;
    }
}
