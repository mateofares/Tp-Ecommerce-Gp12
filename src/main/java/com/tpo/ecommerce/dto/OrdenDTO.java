package com.tpo.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrdenDTO {
    private Long id;
    private Long compradorId;
    private List<ItemOrdenDTO> items;
    private double total;
}
