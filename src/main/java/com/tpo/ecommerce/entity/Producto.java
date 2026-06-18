package com.tpo.ecommerce.entity;

import com.tpo.ecommerce.enums.Categorias;
import com.tpo.ecommerce.enums.Color;
import com.tpo.ecommerce.enums.Estado;
import com.tpo.ecommerce.enums.EstadoProducto;
import com.tpo.ecommerce.enums.EstadoRegistro;
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
    @Column(name = "id")
    private Long id;

    @Column(name = "titulo")
    private String titulo;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "precio")
    private Double precio;

    @Enumerated(EnumType.STRING)
    @Column(name = "categoria")
    private Categorias categoria;

    @Enumerated(EnumType.STRING)
    @Column(name = "marca")
    private Marca marca;

    @Enumerated(EnumType.STRING)
    @Column(name = "talle")
    private Talle talle;

    @Enumerated(EnumType.STRING)
    @Column(name = "color")
    private Color color;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado")
    private Estado estado;

    // Tipo TEXT para poder almacenar imagenes en base64 (data URL), no solo URLs cortas.
    @Column(name = "imagen_url", columnDefinition = "TEXT")
    private String imagenUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_registro")
    private EstadoRegistro estadoRegistro = EstadoRegistro.ACTIVO;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_producto", nullable = false)
    private EstadoProducto estadoProducto = EstadoProducto.DISPONIBLE;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToOne()
    @JoinColumn(name = "descuento_id")
    private Descuento descuento;
}
