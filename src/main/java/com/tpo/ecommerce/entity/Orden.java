package com.tpo.ecommerce.entity;

import com.tpo.ecommerce.enums.EstadoOrden;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "orden")
public class Orden {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "comprador_id")
    private Usuario comprador;

    @ManyToOne(fetch = FetchType.LAZY)  // ← UNA orden usa UNA dirección
    @JoinColumn(name = "direccion_id", nullable = false)  // ← la FK es 'direccion_id'
    private Direccion direccion;  // ← La dirección donde se envía

    @OneToMany(mappedBy = "orden", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemOrden> items = new ArrayList<>();

    @OneToOne(mappedBy = "orden", fetch = FetchType.LAZY)
    private Factura factura;

    @OneToOne(mappedBy = "orden", fetch = FetchType.LAZY)
    private Envio envio;

    @Column(nullable = false)
    private LocalDateTime fecha;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoOrden estado;

    @Column(nullable = false)
    private double total;


}
