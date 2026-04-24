package com.tpo.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CarritoConDescuentoDTO {
    private Long id;
    private Long compradorId;
    private List<ItemCarritoDTO> items;
    private DescuentoDTO descuento;
    
    private Double subtotal;
    private Double descuentoMonto;
    private Double total;


    public void calcularSubtotal(Double precioUnitario) {
        if (this.subtotal == null) {
            this.subtotal = 0.0;
        }
        this.subtotal += precioUnitario;
    }

    /**
     * Aplica el descuento y calcula el total
     */
    public void aplicarDescuento(Double descuentoAplicado) {
        this.descuentoMonto = descuentoAplicado;
        this.total = (this.subtotal != null ? this.subtotal : 0.0) - descuentoAplicado;
    }

    /**
     * Si no hay descuento, el total es igual al subtotal
     */
    public void sinDescuento() {
        this.descuentoMonto = 0.0;
        this.total = this.subtotal != null ? this.subtotal : 0.0;
    }
}
