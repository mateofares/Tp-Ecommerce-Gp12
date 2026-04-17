package com.tpo.ecommerce.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Entity
@Table(name = "factura")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Factura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "factura_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orden_id", nullable = false, unique = true)
    private Orden orden;

    @Column(name = "numero_factura", nullable = false, unique = true, updatable = false)
    private String numeroFactura;

    @Column(name = "nombre_comprador", nullable = false, updatable = false)
    private String nombreComprador;

    @Column(name = "apellido_comprador", nullable = false, updatable = false)
    private String apellidoComprador;

    @Column(name = "cuit_dni", updatable = false)
    private String cuitDni;

    @Column(name = "numero_orden", nullable = false, updatable = false)
    private String numeroOrden;

    @Column(name = "detalles_items", columnDefinition = "TEXT", updatable = false)
    private String detallesItems;

    @Column(name = "fecha_factura", nullable = false, updatable = false)
    private LocalDateTime fechaFactura;

    @Column(name = "total_facturado", nullable = false, updatable = false)
    private Double totalFacturado;

    @Column(name = "url_pdf")
    private String urlPdf;

    @Column(name = "descripcion_impositiva", columnDefinition = "TEXT", updatable = false)
    private String descripcionImpositiva;

    @Column(nullable = false, updatable = false)
    private Boolean activa;

    @OneToMany(mappedBy = "factura", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemFactura> items = new ArrayList<>();
    public Factura(Orden orden, String numeroFactura, String nombreComprador, String apellidoComprador, String cuitDni, String numeroOrden, String detallesItems, Double totalFacturado, String descripcionImpositiva) {
        this.orden = orden;
        this.numeroFactura = numeroFactura;
        this.nombreComprador = nombreComprador;
        this.apellidoComprador = apellidoComprador;
        this.cuitDni = cuitDni;
        this.numeroOrden = numeroOrden;
        this.detallesItems = detallesItems;
        this.fechaFactura = LocalDateTime.now();
        this.totalFacturado = totalFacturado;
        this.descripcionImpositiva = descripcionImpositiva;
        this.activa = true;
    }
}
