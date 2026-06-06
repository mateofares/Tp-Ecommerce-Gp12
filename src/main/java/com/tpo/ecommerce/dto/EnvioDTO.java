package com.tpo.ecommerce.dto;

import lombok.*;
import com.tpo.ecommerce.enums.EstadoEnvio;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class EnvioDTO {
    
    private Long id;
    private Long ordenId;
    private String cliente;
    private String direccion;
    private String transportista;
    private String numSeguimiento;
    private LocalDateTime fechaEstimada;
    private EstadoEnvio estado;
    private LocalDateTime fechaEntrega;

    public EnvioDTO(Long ordenId, String transportista, String numSeguimiento,
                    LocalDateTime fechaEstimada) {
        this.ordenId = ordenId;
        this.transportista = transportista;
        this.numSeguimiento = numSeguimiento;
        this.fechaEstimada = fechaEstimada;
        this.estado = EstadoEnvio.PENDIENTE;
    }


    public EnvioDTO(Long id, Long ordenId, String transportista, String numSeguimiento, LocalDateTime fechaEstimada, EstadoEnvio estado, LocalDateTime fechaEntrega) {
        this.id = id;
        this.ordenId = ordenId;
        this.transportista = transportista;
        this.numSeguimiento = numSeguimiento;
        this.fechaEstimada = fechaEstimada;
        this.estado = estado;
        this.fechaEntrega = fechaEntrega;
    }
}