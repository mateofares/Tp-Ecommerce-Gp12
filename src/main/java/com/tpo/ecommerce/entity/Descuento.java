package com.tpo.ecommerce.entity;

import com.tpo.ecommerce.enums.TipoDescuento;
import com.tpo.ecommerce.enums.EstadoRegistro;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "descuento")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Descuento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "descuento_id")
    private Long id;

    @Column(name = "codigo_descuento", unique = true, nullable = false)
    private String codigoDescuento;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    private TipoDescuento tipo;

    @Column(name = "valor", nullable = false)
    private Double valor;

    @Column(name = "valido_desde", nullable = false)
    private LocalDate validoDesde;

    @Column(name = "valido_hasta", nullable = false)
    private LocalDate validoHasta;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_registro")
    private EstadoRegistro estadoRegistro = EstadoRegistro.ACTIVO;

    public Descuento(String codigoDescuento, TipoDescuento tipo, Double valor,
                     LocalDate validoDesde, LocalDate validoHasta) {
        this.codigoDescuento = codigoDescuento;
        this.tipo = tipo;
        this.valor = valor;
        this.validoDesde = validoDesde;
        this.validoHasta = validoHasta;
        this.estadoRegistro = EstadoRegistro.ACTIVO;
    }
}
