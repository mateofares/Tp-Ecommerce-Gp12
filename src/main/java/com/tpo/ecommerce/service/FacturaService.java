package com.tpo.ecommerce.service;

import com.tpo.ecommerce.dto.FacturaDTO;
import com.tpo.ecommerce.entity.Factura;
import com.tpo.ecommerce.entity.Orden;
import com.tpo.ecommerce.entity.ItemOrden;
import com.tpo.ecommerce.entity.ItemFactura;
import com.tpo.ecommerce.entity.Usuario;
import com.tpo.ecommerce.enums.EstadoOrden;
import com.tpo.ecommerce.exceptions.BadRequestException;
import com.tpo.ecommerce.exceptions.NotFoundException;
import com.tpo.ecommerce.mapper.MapperFactura;
import com.tpo.ecommerce.repository.FacturaRepository;
import com.tpo.ecommerce.repository.ItemFacturaRepository;
import com.tpo.ecommerce.repository.OrdenRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Service
public class FacturaService implements IFacturaService {

    private final FacturaRepository facturaRepository;
    private final ItemFacturaRepository itemFacturaRepository;
    private final OrdenRepository ordenRepository;
    private final MapperFactura mapperFactura;
    private final ObjectMapper objectMapper;

    @Override
    public FacturaDTO crearFacturaAutomatica(Long ordenId) {
        Orden orden = ordenRepository.findById(ordenId)
                .orElseThrow(() -> new NotFoundException("Orden no encontrada con id: " + ordenId));

        if (!EstadoOrden.PAGADA.equals(orden.getEstado())) {
            throw new BadRequestException("La orden debe estar pagada para generar factura. Estado actual: " + orden.getEstado());
        }

        if (facturaRepository.findByOrdenId(ordenId).isPresent()) {
            throw new BadRequestException("Ya existe factura para la orden " + ordenId);
        }

        Usuario usuario = orden.getComprador();
        if (usuario == null) {
            throw new BadRequestException("La orden no tiene comprador asociado");
        }

        String numeroFactura = generarNumeroFactura();
        String detallesItemsJson = serializarItemsAJson(orden.getItems());

        Factura factura = new Factura(
                orden,
                numeroFactura,
                usuario.getNombre(),
                usuario.getApellido(),
                null,
                orden.getId().toString(),
                detallesItemsJson,
                orden.getTotal(),
                null
        );

        factura = facturaRepository.save(factura);

        // CREAR ITEM_FACTURA PARA CADA ITEM_ORDEN
        List<ItemFactura> itemsFactura = new ArrayList<>();
        for (ItemOrden itemOrden : orden.getItems()) {
            ItemFactura itemFactura = new ItemFactura(
                    factura,
                    itemOrden.getProductoTitulo(),
                    itemOrden.getPrecioUnitario()
            );
            itemsFactura.add(itemFactura);
        }
        itemFacturaRepository.saveAll(itemsFactura);
        factura.setItems(itemsFactura);

        return mapperFactura.toDto(factura);
    }

    @Override
    public FacturaDTO obtenerPorId(Long id) {
        Factura factura = facturaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Factura no encontrada con id: " + id));
        return mapperFactura.toDto(factura);
    }

    @Override
    public List<FacturaDTO> listarTodas() {
        return facturaRepository.findAll()
                .stream()
                .map(mapperFactura::toDto)
                .toList();
    }

    @Override
    public FacturaDTO obtenerPorOrden(Long ordenId) {
        if (!ordenRepository.existsById(ordenId)) {
            throw new NotFoundException("Orden no encontrada con id: " + ordenId);
        }

        Factura factura = facturaRepository.findByOrdenId(ordenId)
                .orElseThrow(() -> new NotFoundException("Factura no encontrada para la orden: " + ordenId));
        return mapperFactura.toDto(factura);
    }

    @Override
    public String descargarPDF(Long id) {
        Factura factura = facturaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Factura no encontrada con id: " + id));

        if (factura.getUrlPdf() != null && !factura.getUrlPdf().isBlank()) {
            return factura.getUrlPdf();
        }

        String urlPdf = generarPDF(factura);
        factura.setUrlPdf(urlPdf);
        facturaRepository.save(factura);

        return urlPdf;
    }

    @Override
    public void anularFactura(Long id) {
        Factura factura = facturaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Factura no encontrada con id: " + id));

        if (!factura.getActiva()) {
            throw new BadRequestException("La factura ya está anulada");
        }

        factura.setActiva(false);
        facturaRepository.save(factura);
    }

    private String generarNumeroFactura() {
        int anio = LocalDateTime.now().getYear();
        Long countAnio = facturaRepository.countByAnio(anio);
        Long numeroSecuencial = countAnio + 1;
        return String.format("FAC-%d-%05d", anio, numeroSecuencial);
    }

        private String serializarItemsAJson(List<ItemOrden> items) {
        try {
            List<com.tpo.ecommerce.dto.ItemOrdenDTO> dtos = items.stream()
                    .map(item -> new com.tpo.ecommerce.dto.ItemOrdenDTO(
                            item.getId(),
                            item.getProducto().getId(),
                            item.getProductoTitulo(),
                            item.getPrecioUnitario()
                    ))
                    .toList();

            return objectMapper.writeValueAsString(dtos);
        } catch (Exception e) {
            throw new BadRequestException("Error al serializar items a JSON: " + e.getMessage());
        }
    }
    

    private String generarPDF(Factura factura) {
        try {
            return "https://storage.example.com/facturas/" + factura.getNumeroFactura() + ".pdf";
        } catch (Exception e) {
            throw new BadRequestException("Error al generar PDF: " + e.getMessage());
        }
    }
}
