package com.tpo.ecommerce.service;

import com.tpo.ecommerce.dto.CarritoDTO;
import com.tpo.ecommerce.dto.ItemCarritoDTO;
import com.tpo.ecommerce.dto.OrdenDTO;
import com.tpo.ecommerce.entity.Carrito;
import com.tpo.ecommerce.entity.ItemCarrito;
import com.tpo.ecommerce.entity.ItemOrden;
import com.tpo.ecommerce.entity.Orden;
import com.tpo.ecommerce.entity.Producto;
import com.tpo.ecommerce.entity.Usuario;
import com.tpo.ecommerce.enums.EstadoOrden;
import com.tpo.ecommerce.enums.EstadoProducto;
import com.tpo.ecommerce.exceptions.BadRequestException;
import com.tpo.ecommerce.exceptions.DuplicateResourceException;
import com.tpo.ecommerce.exceptions.NotFoundException;
import com.tpo.ecommerce.mapper.MapperCarrito;
import com.tpo.ecommerce.mapper.MapperOrden;
import com.tpo.ecommerce.entity.Direccion;
import com.tpo.ecommerce.mapper.MapperProducto;
import com.tpo.ecommerce.repository.CarritoRepository;
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
import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor
public class CarritoService implements ICarritoService {

    private final CarritoRepository carritoRepository;
    private final UsuarioRepository usuarioRepository;
    private final ProductoRepository productoRepository;
    private final OrdenRepository ordenRepository;
    private final DireccionRepository direccionRepository;
    private final DescuentoRepository descuentoRepository;
    private final MapperCarrito mapperCarrito;
    private final MapperOrden mapperOrden;
    private final IPagoService pagoService;
    private final IDescuentoService descuentoService;

    @Override
    @Transactional
    public CarritoDTO agregar(CarritoDTO dto) {
        validarRequestConItems(dto);
        validarProductosUnicos(dto.getItems());

        Carrito carrito = obtenerOCrearCarrito(dto.getCompradorId());

        for (ItemCarritoDTO itemDTO : dto.getItems()) {
            Producto producto = obtenerProducto(itemDTO.getProductoId());
            boolean yaEstaEnCarrito = carrito.getItems().stream()
                    .anyMatch(item -> item.getProducto().getId().equals(producto.getId()));
            if (yaEstaEnCarrito) {
                throw new BadRequestException("El producto '" + producto.getTitulo() + "' ya está en el carrito");
            }
            ItemCarrito item = new ItemCarrito();
            item.setCarrito(carrito);
            item.setProducto(producto);
            carrito.getItems().add(item);
        }

        return mapperCarrito.toDto(carritoRepository.save(carrito));
    }

    @Override
    @Transactional
    public CarritoDTO eliminar(CarritoDTO dto) {
        validarRequestConItems(dto);
        validarProductosUnicos(dto.getItems());

        Carrito carrito = obtenerOCrearCarrito(dto.getCompradorId());

        for (ItemCarritoDTO itemDTO : dto.getItems()) {
            if (itemDTO.getProductoId() == null) {
                throw new BadRequestException("Cada item debe indicar productoId");
            }
            carrito.getItems().stream()
                    .filter(item -> item.getProducto().getId().equals(itemDTO.getProductoId()))
                    .findFirst()
                    .ifPresent(carrito.getItems()::remove);
        }

        return mapperCarrito.toDto(carritoRepository.save(carrito));
    }

    @Override
    @Transactional
    public CarritoDTO vaciar(CarritoDTO dto) {
        if (dto == null || dto.getCompradorId() == null) {
            throw new BadRequestException("Debe indicar el compradorId");
        }

        Carrito carrito = obtenerOCrearCarrito(dto.getCompradorId());
        carrito.getItems().clear();
        return mapperCarrito.toDto(carritoRepository.save(carrito));
    }

    @Override
    @Transactional
    public CarritoDTO ver(Long compradorId) {
        if (compradorId == null) {
            throw new BadRequestException("Debe indicar el compradorId");
        }
        return mapperCarrito.toDto(obtenerOCrearCarrito(compradorId));
    }

    @Override
    @Transactional
    public OrdenDTO comprar(CarritoDTO dto) {
        if (dto == null || dto.getCompradorId() == null) {
            throw new BadRequestException("Debe indicar el compradorId");
        }
        if (dto.getDireccionId() == null) {
            throw new BadRequestException("Debe indicar la dirección de envío");
        }

        Carrito carrito = obtenerOCrearCarrito(dto.getCompradorId());
        if (carrito.getItems().isEmpty()) {
            throw new BadRequestException("El carrito esta vacio");
        }

        Direccion direccion = direccionRepository.findById(dto.getDireccionId())
                .orElseThrow(() -> new NotFoundException("Dirección no encontrada"));
        if (!direccion.getUsuario().getId().equals(dto.getCompradorId())) {
            throw new BadRequestException("La dirección no pertenece al comprador");
        }

        Orden orden = new Orden();
        orden.setComprador(carrito.getComprador());
        orden.setDireccion(direccion);
        orden.setFecha(LocalDateTime.now());
        orden.setEstado(EstadoOrden.CONFIRMADA);

        List<ItemOrden> itemsOrden = new ArrayList<>();
        double subtotal = 0D;

        for (ItemCarrito itemCarrito : carrito.getItems()) {
            Producto producto = itemCarrito.getProducto();
            double precioUnitario = MapperProducto.calcularPrecioEfectivo(producto.getPrecio(), producto.getDescuento());

            ItemOrden itemOrden = new ItemOrden();
            itemOrden.setOrden(orden);
            itemOrden.setProducto(producto);
            itemOrden.setProductoTitulo(producto.getTitulo());
            itemOrden.setPrecioUnitario(precioUnitario);
            itemsOrden.add(itemOrden);

            subtotal += precioUnitario;
        }

        orden.setItems(itemsOrden);
        orden.setTotal(subtotal);

        Orden ordenGuardada = ordenRepository.save(orden);
        pagoService.crearPendientePorOrden(ordenGuardada.getId());

        carrito.setDescuento(null);
        carrito.getItems().clear();
        carritoRepository.save(carrito);

        return mapperOrden.toDto(ordenGuardada);
    }

    private Carrito obtenerOCrearCarrito(Long compradorId) {
        Usuario comprador = obtenerComprador(compradorId);
        return carritoRepository.findByCompradorId(compradorId)
                .orElseGet(() -> carritoRepository.save(crearCarrito(comprador)));
    }

    private Carrito crearCarrito(Usuario comprador) {
        Carrito carrito = new Carrito();
        carrito.setComprador(comprador);
        carrito.setItems(new ArrayList<>());
        return carrito;
    }

    private Usuario obtenerComprador(Long compradorId) {
        return usuarioRepository.findById(compradorId)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));
    }

    private Producto obtenerProducto(Long productoId) {
        if (productoId == null) {
            throw new BadRequestException("Cada item debe indicar productoId");
        }
        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new NotFoundException("Producto no encontrado"));
        if (producto.getEstadoProducto() != EstadoProducto.DISPONIBLE) {
            throw new BadRequestException("El producto '" + producto.getTitulo() + "' ya no está disponible");
        }
        return producto;
    }

    private void validarRequestConItems(CarritoDTO dto) {
        if (dto == null || dto.getCompradorId() == null) {
            throw new BadRequestException("Debe indicar el compradorId");
        }
        if (dto.getItems() == null || dto.getItems().isEmpty()) {
            throw new BadRequestException("Debe enviar al menos un item");
        }
    }

    private void validarProductosUnicos(List<ItemCarritoDTO> items) {
        if (items == null || items.isEmpty()) {
            return;
        }
        Set<Long> vistos = new HashSet<>();
        for (ItemCarritoDTO item : items) {
            Long productoId = item.getProductoId();
            if (productoId == null) {
                continue;
            }
            if (!vistos.add(productoId)) {
                throw new DuplicateResourceException("Producto", "id", productoId);
            }
        }
    }

    @Override
    @Transactional
    public CarritoDTO aplicarDescuentoAlCarrito(Long compradorId, Long descuentoId) {
        Carrito carrito = obtenerOCrearCarrito(compradorId);

        // calcularDescuentoSinValidar verifica existencia; validarDescuentoAplicable verifica vigencia
        if (!descuentoService.validarDescuentoAplicable(descuentoId)) {
            throw new BadRequestException("El descuento no es válido o ha expirado");
        }

        carrito.setDescuento(descuentoRepository.getReferenceById(descuentoId));

        return mapperCarrito.toDto(carritoRepository.save(carrito));
    }

    @Override
    @Transactional
    public CarritoDTO removerDescuentoDelCarrito(Long compradorId) {
        Carrito carrito = obtenerOCrearCarrito(compradorId);
        carrito.setDescuento(null);
        return mapperCarrito.toDto(carritoRepository.save(carrito));
    }
}
