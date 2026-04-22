package com.tpo.ecommerce.entity;

import com.tpo.ecommerce.enums.EstadoPago;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "pago")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne()
    @JoinColumn(name = "orden_id", nullable = false, unique = true)
    private Orden orden;

    @Column(name = "metodo")
    private String metodo;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado")
    private EstadoPago estado;

    @Column(name = "monto")
    private Double monto;

    @Column(name = "referencia_maxima")
    private String referenciaMaxima;

    @Column(name = "fecha_pago")
    private LocalDateTime fechaPago;
}
