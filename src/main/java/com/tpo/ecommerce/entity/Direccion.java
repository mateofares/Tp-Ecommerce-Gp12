package com.tpo.ecommerce.entity;

import com.tpo.ecommerce.enums.EstadoRegistro;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "direccion")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Direccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "direccion_id")
    private Long id;

    @ManyToOne()
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(nullable = false)
    private String calle;

    @Column(nullable = false)
    private String numero;

    @Column(nullable = false)
    private String ciudad;

    @Column(name = "codigo_postal", nullable = false)
    private String codigoPostal;

    @Column(nullable = false)
    private String provincia;

    @Column(name = "tipo_direccion", nullable = false)
    private String tipoDireccion;

    @Column(nullable = false)
    private Boolean predeterminada;

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(nullable = false)
    private Boolean activa;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_registro")
    private EstadoRegistro estadoRegistro = EstadoRegistro.ACTIVO;

    private String notas;

    @OneToMany(mappedBy = "direccion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Orden> ordenes = new ArrayList<>();

    public Direccion(Usuario usuario, String calle, String numero, String ciudad,
                     String codigoPostal, String provincia, String tipoDireccion,
                     Boolean predeterminada, String notas) {
        this.usuario = usuario;
        this.calle = calle;
        this.numero = numero;
        this.ciudad = ciudad;
        this.codigoPostal = codigoPostal;
        this.provincia = provincia;
        this.tipoDireccion = tipoDireccion;
        this.predeterminada = predeterminada;
        this.notas = notas;
        this.fechaCreacion = LocalDateTime.now();
        this.activa = true;
        this.estadoRegistro = EstadoRegistro.ACTIVO;
    }
}
