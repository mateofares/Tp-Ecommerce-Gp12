package com.tpo.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ReseniaDTO {
    private Long id;
    private Long productoId;
    private Long compradorId;
    private Long vendedorId;
    private Long ordenId;
    private Integer calificacion;
    private String comentarios;
    private LocalDateTime fecha;
    private Boolean verificado;
}
