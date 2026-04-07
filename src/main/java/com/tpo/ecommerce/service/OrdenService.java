package com.tpo.ecommerce.service;

import com.tpo.ecommerce.dto.ItemOrdenDTO;
import com.tpo.ecommerce.dto.OrdenDTO;
import com.tpo.ecommerce.entity.ItemOrden;
import com.tpo.ecommerce.entity.Orden;
import com.tpo.ecommerce.entity.Producto;
import com.tpo.ecommerce.entity.Usuario;
import com.tpo.ecommerce.enums.EstadoOrden;
import com.tpo.ecommerce.exceptions.BadRequestException;
import com.tpo.ecommerce.exceptions.DuplicateResourceException;
import com.tpo.ecommerce.exceptions.NotFoundException;
import com.tpo.ecommerce.mapper.MapperOrden;
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
    private final MapperOrden mapperOrden;
    private final IPagoService pagoService;

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

        Orden orden = new Orden();
        orden.setComprador(comprador);
        orden.setFecha(LocalDateTime.now());
        orden.setEstado(EstadoOrden.CONFIRMADA);

        List<ItemOrden> itemsOrden = new ArrayList<>();
        double total = 0D;

        for (ItemOrdenDTO itemDTO : dto.getItems()) {
            if (itemDTO.getProductoId() == null) {
                throw new BadRequestException("Cada item debe indicar productoId");
            }
            if (itemDTO.getCantidad() == null || itemDTO.getCantidad() <= 0) {
                throw new BadRequestException("La cantidad debe ser mayor a cero");
            }

            Producto producto = productoRepository.findById(itemDTO.getProductoId())
                    .orElseThrow(() -> new NotFoundException("Producto no encontrado"));

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

}
