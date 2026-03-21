package com.tpo.ecommerce.entity;

import com.tpo.ecommerce.enums.Categorias;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "producto")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private String titulo;
    private String descripcion;
    private Double precio;
    private Categorias categoria;
    private Marca marca;
    private Talle talle;
    private Color color;
    private Estado estado;
    private String imagenUrl;

    public Producto(String titulo, String descripcion, Double precio, Categorias categoria, String marca, String talle, String color, String estado, String imagenUrl) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.precio = precio;
        this.categoria = categoria;
        this.marca = marca;
        this.talle = talle;
        this.color = color;
        this.estado = estado;
        this.imagenUrl = imagenUrl;
    }
}
