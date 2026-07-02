package com.tpo.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ItemOrdenDTO {
    private Long id;
    private Long productoId;
    private String productoTitulo;
    private Double precioUnitario;
    private String imagenUrl;
}
