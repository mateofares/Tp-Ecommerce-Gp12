package com.tpo.ecommerce.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "item_factura")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ItemFactura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_factura_id")
    private Long id;

    @ManyToOne()
    @JoinColumn(name = "factura_id", nullable = false)
    private Factura factura;

    @Column(name = "producto_titulo", nullable = false, updatable = false)
    private String productoTitulo;

    @Column(name = "precio_unitario", nullable = false, updatable = false)
    private Double precioUnitario;

    public ItemFactura(Factura factura, String productoTitulo, Double precioUnitario) {
        this.factura = factura;
        this.productoTitulo = productoTitulo;
        this.precioUnitario = precioUnitario;
    }
}
