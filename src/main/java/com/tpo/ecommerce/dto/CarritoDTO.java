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
public class CarritoDTO {
    private Long id;
    private Long compradorId;
    private List<ItemCarritoDTO> items;
    private double total;
}   
