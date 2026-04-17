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
    private Integer cantidad;

    public ItemFacturaDTO(Long facturaId, String productoTitulo,
                          Double precioUnitario, Integer cantidad) {
        this.facturaId = facturaId;
        this.productoTitulo = productoTitulo;
        this.precioUnitario = precioUnitario;
        this.cantidad = cantidad;
    }
}
