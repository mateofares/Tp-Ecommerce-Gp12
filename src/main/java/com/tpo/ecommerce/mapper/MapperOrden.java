package com.tpo.ecommerce.mapper;

import com.tpo.ecommerce.dto.ItemOrdenDTO;
import com.tpo.ecommerce.dto.OrdenDTO;
import com.tpo.ecommerce.entity.Direccion;
import com.tpo.ecommerce.entity.ItemOrden;
import com.tpo.ecommerce.entity.Orden;
import com.tpo.ecommerce.entity.Pago;
import com.tpo.ecommerce.repository.EnvioRepository;
import com.tpo.ecommerce.repository.PagoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MapperOrden {

    private final PagoRepository pagoRepository;
    private final EnvioRepository envioRepository;

    public OrdenDTO toDto(Orden orden) {
        List<ItemOrdenDTO> items = orden.getItems().stream()
                .sorted(Comparator.comparing(ItemOrden::getId, Comparator.nullsLast(Long::compareTo)))
                .map(item -> new ItemOrdenDTO(
                        item.getId(),
                        item.getProducto().getId(),
                        item.getProductoTitulo(),
                        item.getPrecioUnitario(),
                        item.getProducto().getImagenUrl()
                ))
                .toList();

        OrdenDTO dto = new OrdenDTO();
        dto.setId(orden.getId());
        dto.setCompradorId(orden.getComprador().getId());
        dto.setItems(items);
        dto.setEstado(orden.getEstado());
        dto.setTotal(orden.getTotal());
        dto.setFecha(orden.getFecha());

        if (orden.getDireccion() != null) {
            Direccion d = orden.getDireccion();
            dto.setDireccionId(d.getId());
            dto.setDireccionResumen(d.getCalle() + " " + d.getNumero() + ", " + d.getCiudad() + ", " + d.getProvincia());
        }
        if (orden.getDescuento() != null) {
            dto.setDescuentoId(orden.getDescuento().getId());
            dto.setDescuentoAplicado(orden.getDescuentoAplicado());
        }

        pagoRepository.findByOrdenId(orden.getId()).ifPresent(pago ->
                dto.setMetodoPago(pago.getMetodo() != null ? pago.getMetodo().name() : null)
        );

        envioRepository.findByOrdenId(orden.getId()).ifPresent(envio ->
                dto.setEstadoEnvio(envio.getEstado().name())
        );

        return dto;
    }
}
