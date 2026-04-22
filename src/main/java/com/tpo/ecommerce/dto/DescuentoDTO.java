package com.tpo.ecommerce.dto;

import com.tpo.ecommerce.enums.TipoDescuento;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DescuentoDTO {
    private Long id;
    private String codigoDescuento;
    private TipoDescuento tipo;
    private Double valor;
    private LocalDate validoDesde;
    private LocalDate validoHasta;
}
