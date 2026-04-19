package com.tpo.ecommerce.dto;

import com.tpo.ecommerce.enums.EstadoOrden;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrdenDTO {
    private Long id;
    private Long compradorId;
    private List<ItemOrdenDTO> items;
    private EstadoOrden estado;
    private double total;
    private Long descuentoId;
    private Double descuentoAplicado;
}
