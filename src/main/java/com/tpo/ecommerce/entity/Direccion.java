package com.tpo.ecommerce.entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Entity
@Table(name = "direccion")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor


public class Direccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String calle;
    private String ciudad;
    private String provincia;
    private int codigoPostal;
    private String pais;
    private boolean es_Dir_principal;
    
}

@ManyToOnepackage com.tpo.ecommerce.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "direccion")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Direccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "direccion_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(nullable = false)
    private String calle;

    @Column(nullable = false)
    private String numero;

    @Column(nullable = false)
    private String ciudad;

    @Column(name = "codigo_postal", nullable = false)
    private String codigoPostal;

    @Column(nullable = false)
    private String provincia;

    @Column(name = "tipo_direccion", nullable = false)
    private String tipoDireccion;

    @Column(nullable = false)
    private Boolean predeterminada;

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(nullable = false)
    private Boolean activa;

    @OneToMany(mappedBy = "direccion")
    private List<Orden> ordenes = new ArrayList<>();

    // Constructor personalizado para crear dirección sin pasar ID ni fechaCreacion
    public Direccion(Usuario usuario, String calle, String numero, String ciudad, String codigoPostal, String provincia,  String tipoDireccion, Boolean predeterminada, String notas) {
        this.usuario = usuario;
        this.calle = calle;
        this.numero = numero;
        this.ciudad = ciudad;
        this.codigoPostal = codigoPostal;
        this.provincia = provincia;
        this.tipoDireccion = tipoDireccion;
        this.predeterminada = predeterminada;
        this.notas = notas;
        this.fechaCreacion = LocalDateTime.now();
        this.activa = true;
    }
}
@JoinColumn(name = "usuario_id")
private Usuario usuario;

@OneToMany(mappedBy = "direccion", cascade = CascadeType.ALL, orphanRemoval = true)
private List<Orden> ordenes;


private Direccion(String calle, String ciudad, String provincia, int codigoPostal, String pais, boolean es_Dir_principal) {
    this.calle = calle;
    this.ciudad = ciudad;
    this.provincia = provincia;
    this.codigoPostal = codigoPostal;
    this.pais = pais;
    this.es_Dir_principal = es_Dir_principal;
}

