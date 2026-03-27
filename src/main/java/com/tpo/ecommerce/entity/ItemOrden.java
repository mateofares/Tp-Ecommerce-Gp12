package com.tpo.ecommerce.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "item_orden")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ItemOrden {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "orden_id", nullable = false)
    private Orden orden;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    @Column(nullable = false)
    private String productoTitulo;

    @Column(nullable = false)
    private Double precioUnitario;

    @Column(nullable = false)
    private Integer cantidad;
}
