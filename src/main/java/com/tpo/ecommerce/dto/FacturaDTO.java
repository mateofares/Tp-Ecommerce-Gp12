package com.tpo.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FacturaDTO {

    private Long id;
    private Long ordenId;
    private String numeroFactura;
    private String nombreComprador;
    private String apellidoComprador;
    private String cuitDni;
    private String numeroOrden;
    private String detallesItems;
    private LocalDateTime fechaFactura;
    private Double totalFacturado;
    private String urlPdf;
    private String descripcionImpositiva;
    private Boolean activa;

    public FacturaDTO(Long ordenId, String numeroFactura, String nombreComprador,
                      String apellidoComprador, String cuitDni, String numeroOrden,
                      String detallesItems, LocalDateTime fechaFactura,
                      Double totalFacturado, String descripcionImpositiva, Boolean activa) {
        this.ordenId = ordenId;
        this.numeroFactura = numeroFactura;
        this.nombreComprador = nombreComprador;
        this.apellidoComprador = apellidoComprador;
        this.cuitDni = cuitDni;
        this.numeroOrden = numeroOrden;
        this.detallesItems = detallesItems;
        this.fechaFactura = fechaFactura;
        this.totalFacturado = totalFacturado;
        this.descripcionImpositiva = descripcionImpositiva;
        this.activa = activa;
    }
}
