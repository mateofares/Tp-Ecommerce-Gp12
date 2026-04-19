package com.tpo.ecommerce.dto;

import com.tpo.ecommerce.enums.TipoDescuento;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class DescuentoDTO {
    private Long id;
    private String codigoDescuento;
    private TipoDescuento tipo;
    private Double valor;
    private LocalDate validoDesde;
    private LocalDate validoHasta;
    private Integer maximoDeUsos;
    private Integer usosActuales;

    public DescuentoDTO(String codigoDescuento, TipoDescuento tipo, Double valor, LocalDate validoDesde,
                        LocalDate validoHasta, Integer maximoDeUsos) {
        this.codigoDescuento = codigoDescuento;
        this.tipo = tipo;
        this.valor = valor;
        this.validoDesde = validoDesde;
        this.validoHasta = validoHasta;
        this.maximoDeUsos = maximoDeUsos;
        this.usosActuales = 0;
    }
}
