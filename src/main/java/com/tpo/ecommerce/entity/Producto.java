package com.tpo.ecommerce.entity;

import com.tpo.ecommerce.enums.Categorias;
import com.tpo.ecommerce.enums.Color;
import com.tpo.ecommerce.enums.Estado;
import com.tpo.ecommerce.enums.Marca;
import com.tpo.ecommerce.enums.Talle;
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
    private String titulo;
    private String descripcion;
    private Double precio;

    @Enumerated(EnumType.STRING)
    private Categorias categoria;

    @Enumerated(EnumType.STRING)
    private Marca marca;

    @Enumerated(EnumType.STRING)
    private Talle talle;

    @Enumerated(EnumType.STRING)
    private Color color;

    @Enumerated(EnumType.STRING)
    private Estado estado;

    private String imagenUrl;
}
