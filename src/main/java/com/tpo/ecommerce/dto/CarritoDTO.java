package com.tpo.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CarritoDTO {
    private Long id;
    private Long compradorId;
    private List<ItemCarritoDTO> items;
    private double total;
}   