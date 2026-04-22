package com.tpo.ecommerce.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "resenia")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Resenia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne()
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    @ManyToOne()
    @JoinColumn(name = "comprador_id", nullable = false)
    private Usuario comprador;

    @ManyToOne()
    @JoinColumn(name = "vendedor_id", nullable = false)
    private Usuario vendedor;

    @ManyToOne()
    @JoinColumn(name = "orden_id", nullable = false)
    private Orden orden;

    @Column(name = "calificacion")
    private Integer calificacion;

    @Column(name = "comentarios", length = 1200)
    private String comentarios;

    @Column(name = "fecha")
    private LocalDateTime fecha;

    @Column(name = "verificado")
    private Boolean verificado;
}
