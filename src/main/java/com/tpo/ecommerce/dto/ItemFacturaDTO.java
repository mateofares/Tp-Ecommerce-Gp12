package com.tpo.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ItemFacturaDTO {

    private Long id;
    private Long facturaId;
    private String productoTitulo;
    private Double precioUnitario;
}
