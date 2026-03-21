package com.tpo.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CarritoDTO {
    private Long id;
    private Long compradorId;
    private List<ItemCarritoDTO> items;
    private double total;
}   