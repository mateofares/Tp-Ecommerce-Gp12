package com.tpo.ecommerce.service;

import com.tpo.ecommerce.dto.ItemOrdenDTO;
import com.tpo.ecommerce.dto.OrdenDTO;
import com.tpo.ecommerce.entity.ItemOrden;
import com.tpo.ecommerce.entity.Orden;
import com.tpo.ecommerce.entity.Producto;
import com.tpo.ecommerce.entity.Usuario;
import com.tpo.ecommerce.exceptions.DuplicateResourceException;
import com.tpo.ecommerce.repository.OrdenRepository;
import com.tpo.ecommerce.repository.ProductoRepository;
import com.tpo.ecommerce.repository.UsuarioRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class OrdenService {

    private final OrdenRepository ordenRepository;
    private final UsuarioRepository usuarioRepository;
    private final ProductoRepository productoRepository;

    @Transactional
    public OrdenDTO comprar(OrdenDTO dto) {
        if (dto == null || dto.getCompradorId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Debe indicar el compradorId");
        }
        if (dto.getItems() == null || dto.getItems().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Debe enviar al menos un item");
        }

        validarProductosUnicos(dto.getItems());
        Usuario comprador = usuarioRepository.findById(dto.getCompradorId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));

        Orden orden = new Orden();
        orden.setComprador(comprador);
        orden.setFecha(LocalDateTime.now());
        orden.setEstado("CONFIRMADA");

        List<ItemOrden> itemsOrden = new ArrayList<>();
        double total = 0D;

        for (ItemOrdenDTO itemDTO : dto.getItems()) {
            if (itemDTO.getProductoId() == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cada item debe indicar productoId");
            }
            if (itemDTO.getCantidad() == null || itemDTO.getCantidad() <= 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La cantidad debe ser mayor a cero");
            }

            Producto producto = productoRepository.findById(itemDTO.getProductoId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado"));

            double precioUnitario = producto.getPrecio() != null ? producto.getPrecio() : 0D;

            ItemOrden itemOrden = new ItemOrden();
            itemOrden.setOrden(orden);
            itemOrden.setProducto(producto);
            itemOrden.setProductoTitulo(producto.getTitulo());
            itemOrden.setPrecioUnitario(precioUnitario);
            itemOrden.setCantidad(itemDTO.getCantidad());
            itemsOrden.add(itemOrden);

            total += precioUnitario * itemDTO.getCantidad();
        }

        orden.setItems(itemsOrden);
        orden.setTotal(total);

        return toDto(ordenRepository.save(orden));
    }

    @Transactional(readOnly = true)
    public List<OrdenDTO> misCompras(Long compradorId) {
        return ordenRepository.findByCompradorIdOrderByFechaDesc(compradorId).stream()
                .map(this::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<OrdenDTO> misVentas(Long vendedorId) {
        return ordenRepository.findDistinctByItemsProductoUsuarioIdOrderByFechaDesc(vendedorId).stream()
                .map(this::toDto)
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

    private OrdenDTO toDto(Orden orden) {
        List<ItemOrdenDTO> items = orden.getItems().stream()
                .sorted(Comparator.comparing(ItemOrden::getId, Comparator.nullsLast(Long::compareTo)))
                .map(item -> new ItemOrdenDTO(
                        item.getId(),
                        item.getProducto().getId(),
                        item.getProductoTitulo(),
                        item.getPrecioUnitario(),
                        item.getCantidad()
                ))
                .toList();

        return new OrdenDTO(
                orden.getId(),
                orden.getComprador().getId(),
                items,
                orden.getTotal()
        );
    }
}
