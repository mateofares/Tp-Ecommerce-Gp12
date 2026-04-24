package com.tpo.ecommerce.service;

import com.tpo.ecommerce.dto.ItemOrdenDTO;
import com.tpo.ecommerce.dto.OrdenDTO;
import com.tpo.ecommerce.entity.ItemOrden;
import com.tpo.ecommerce.entity.Orden;
import com.tpo.ecommerce.entity.Producto;
import com.tpo.ecommerce.entity.Usuario;
import com.tpo.ecommerce.enums.EstadoOrden;
import com.tpo.ecommerce.enums.EstadoProducto;
import com.tpo.ecommerce.exceptions.BadRequestException;
import com.tpo.ecommerce.exceptions.DuplicateResourceException;
import com.tpo.ecommerce.exceptions.NotFoundException;
import com.tpo.ecommerce.mapper.MapperOrden;
import com.tpo.ecommerce.entity.Direccion;
import com.tpo.ecommerce.mapper.MapperProducto;
import com.tpo.ecommerce.repository.DescuentoRepository;
import com.tpo.ecommerce.repository.DireccionRepository;
import com.tpo.ecommerce.repository.OrdenRepository;
import com.tpo.ecommerce.repository.ProductoRepository;
import com.tpo.ecommerce.repository.UsuarioRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class OrdenService implements IOrdenService {

    private final OrdenRepository ordenRepository;
    private final UsuarioRepository usuarioRepository;
    private final ProductoRepository productoRepository;
    private final DireccionRepository direccionRepository;
    private final DescuentoRepository descuentoRepository;
    private final MapperOrden mapperOrden;
    private final IPagoService pagoService;
    private final IDescuentoService descuentoService;

    @Transactional
    public OrdenDTO comprar(OrdenDTO dto) {
        if (dto == null || dto.getCompradorId() == null) {
            throw new BadRequestException("Debe indicar el compradorId");
        }
        if (dto.getItems() == null || dto.getItems().isEmpty()) {
            throw new BadRequestException("Debe enviar al menos un item");
        }

        validarProductosUnicos(dto.getItems());

        Usuario comprador = usuarioRepository.findById(dto.getCompradorId())
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));

        if (dto.getDireccionId() == null) {
            throw new BadRequestException("Debe indicar la dirección de envío");
        }
        Direccion direccion = direccionRepository.findById(dto.getDireccionId())
                .orElseThrow(() -> new NotFoundException("Dirección no encontrada"));
        if (!direccion.getUsuario().getId().equals(dto.getCompradorId())) {
            throw new BadRequestException("La dirección no pertenece al comprador");
        }

        Orden orden = new Orden();
        orden.setComprador(comprador);
        orden.setDireccion(direccion);
        orden.setFecha(LocalDateTime.now());
        orden.setEstado(EstadoOrden.CONFIRMADA);

        List<ItemOrden> itemsOrden = new ArrayList<>();
        double total = 0D;

        for (ItemOrdenDTO itemDTO : dto.getItems()) {
            if (itemDTO.getProductoId() == null) {
                throw new BadRequestException("Cada item debe indicar productoId");
            }
            Producto producto = productoRepository.findById(itemDTO.getProductoId())
                    .orElseThrow(() -> new NotFoundException("Producto no encontrado"));
            if (producto.getEstadoProducto() != EstadoProducto.DISPONIBLE) {
                throw new BadRequestException("El producto '" + producto.getTitulo() + "' ya no está disponible");
            }
            if (producto.getUsuario() != null && producto.getUsuario().getId().equals(dto.getCompradorId())) {
                throw new BadRequestException("No podes comprar tu propio producto");
            }

            double precioUnitario = MapperProducto.calcularPrecioEfectivo(producto.getPrecio(), producto.getDescuento());

            ItemOrden itemOrden = new ItemOrden();
            itemOrden.setOrden(orden);
            itemOrden.setProducto(producto);
            itemOrden.setProductoTitulo(producto.getTitulo());
            itemOrden.setPrecioUnitario(precioUnitario);
            itemsOrden.add(itemOrden);

            total += precioUnitario;
        }

        orden.setItems(itemsOrden);
        orden.setTotal(total);
        Orden ordenGuardada = ordenRepository.save(orden);
        pagoService.crearPendientePorOrden(ordenGuardada.getId());
        return mapperOrden.toDto(ordenGuardada);
    }

    @Transactional(readOnly = true)
    public List<OrdenDTO> misCompras(Long compradorId) {
        return ordenRepository.findByCompradorIdOrderByFechaDesc(compradorId).stream()
                .map(mapperOrden::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<OrdenDTO> misVentas(Long vendedorId) {
        return ordenRepository.findDistinctByItemsProductoUsuarioIdOrderByFechaDesc(vendedorId).stream()
                .map(mapperOrden::toDto)
                .toList();
    }

    private void validarProductosUnicos(List<ItemOrdenDTO> items) {
        if (items == null || items.isEmpty()) {
            return;
        }
        Set<Long> vistos = new HashSet<>();
        for (ItemOrdenDTO item : items) {
            Long productoId = item.getProductoId();
            if (productoId == null) {
                continue;
            }
            if (!vistos.add(productoId)) {
                throw new DuplicateResourceException("Producto", "id", productoId);
            }
        }
    }

    /**
     * Aplica un descuento a una orden
     * @param ordenId ID de la orden
     * @param descuentoId ID del descuento
     * @return Orden actualizada con descuento y total recalculado
     */
    @Override
    @Transactional
    public OrdenDTO aplicarDescuentoAOrden(Long ordenId, Long descuentoId) {
        Orden orden = ordenRepository.findById(ordenId)
                .orElseThrow(() -> new NotFoundException("Orden no encontrada con ID: " + ordenId));

        if (orden.getDescuento() != null) {
            throw new BadRequestException("La orden ya tiene un descuento aplicado. Remuévalo antes de aplicar uno nuevo.");
        }

        // calcularDescuento valida vigencia y lanza excepción si no es aplicable
        Double descuentoMonto = descuentoService.calcularDescuento(orden.getTotal(), descuentoId);

        orden.setDescuento(descuentoRepository.getReferenceById(descuentoId));
        orden.setDescuentoAplicado(descuentoMonto);
        orden.setTotal(orden.getTotal() - descuentoMonto);

        return mapperOrden.toDto(ordenRepository.save(orden));
    }

    /**
     * Elimina el descuento de una orden
     * @param ordenId ID de la orden
     * @return Orden actualizada sin descuento y total original
     */
    @Override
    @Transactional
    public OrdenDTO removerDescuentoDeOrden(Long ordenId) {
        Orden orden = ordenRepository.findById(ordenId)
                .orElseThrow(() -> new NotFoundException("Orden no encontrada con ID: " + ordenId));

        if (orden.getDescuento() != null && orden.getDescuentoAplicado() != null) {
            orden.setTotal(orden.getTotal() + orden.getDescuentoAplicado());
            orden.setDescuento(null);
            orden.setDescuentoAplicado(null);
        }

        return mapperOrden.toDto(ordenRepository.save(orden));
    }

}
