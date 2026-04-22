package com.tpo.ecommerce.entity;

import com.tpo.ecommerce.enums.UserRol;
import com.tpo.ecommerce.enums.EstadoRegistro;
import jakarta.persistence.*;
import lombok.*;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "usuario")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Usuario implements UserDetails {

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

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "estado_registro")
    private EstadoRegistro estadoRegistro = EstadoRegistro.ACTIVO;

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
        this.estadoRegistro = EstadoRegistro.ACTIVO;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(userRol.name()));
    }

    @Override
    public String getPassword() {
        return contrasenia;
    }

    @Override
    public String getUsername() {
        return mail;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
