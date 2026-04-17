package com.tpo.ecommerce.entity;

import jakarta.persistence.*;
import lombok.*;
import com.tpo.ecommerce.enums.EstadoEnvio;
import java.time.LocalDateTime;

@Entity
@Table(name = "envio")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class Envio {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "envio_id")
    private Long id;
    
    @OneToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "orden_id", nullable = false, unique = true)
    private Orden orden;
    
    @Column(name = "transportista", nullable = false, updatable = false)
    private String transportista;
    
    @Column(name = "num_seguimiento", nullable = false, updatable = false, unique = true)
    private String numSeguimiento;
    
    @Column(name = "fecha_estimada")
    private LocalDateTime fechaEstimada;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private EstadoEnvio estado;
    
    @Column(name = "fecha_entrega")
    private LocalDateTime fechaEntrega;
    
    public Envio(Orden orden, String transportista, String numSeguimiento, LocalDateTime fechaEstimada) {
        this.orden = orden;
        this.transportista = transportista;
        this.numSeguimiento = numSeguimiento;
        this.fechaEstimada = fechaEstimada;
        this.estado = EstadoEnvio.PENDIENTE;
    }
}