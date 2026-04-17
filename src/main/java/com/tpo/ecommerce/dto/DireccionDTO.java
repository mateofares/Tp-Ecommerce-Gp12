package com.tpo.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DireccionDTO {

    private Long id;
    private Long usuarioId;
    private String calle;
    private String numero;
    private String ciudad;
    private String codigoPostal;
    private String provincia;
    private String tipoDireccion;
    private Boolean predeterminada;
    private String notas;
    private Boolean activa;
    private LocalDateTime fechaCreacion;

    // Constructor para crear dirección (sin id ni fechaCreacion)
    public DireccionDTO(Long usuarioId, String calle, String numero, String ciudad, String codigoPostal, String provincia, String tipoDireccion, Boolean predeterminada, String notas) {
        this.usuarioId = usuarioId;
        this.calle = calle;
        this.numero = numero;
        this.ciudad = ciudad;
        this.codigoPostal = codigoPostal;
        this.provincia = provincia;
        this.tipoDireccion = tipoDireccion;
        this.predeterminada = predeterminada;
        this.notas = notas;
    }
}