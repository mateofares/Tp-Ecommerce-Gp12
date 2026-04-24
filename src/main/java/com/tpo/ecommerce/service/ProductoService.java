package com.tpo.ecommerce.service;

import com.tpo.ecommerce.dto.ProductoDTO;
import com.tpo.ecommerce.entity.Producto;
import com.tpo.ecommerce.enums.Categorias;
import com.tpo.ecommerce.enums.Color;
import com.tpo.ecommerce.enums.Estado;
import com.tpo.ecommerce.enums.EstadoProducto;
import com.tpo.ecommerce.enums.EstadoRegistro;
import com.tpo.ecommerce.enums.Marca;
import com.tpo.ecommerce.enums.Talle;
import com.tpo.ecommerce.entity.Descuento;
import com.tpo.ecommerce.exceptions.BadRequestException;
import com.tpo.ecommerce.exceptions.NotFoundException;
import com.tpo.ecommerce.exceptions.UnauthorizedException;
import com.tpo.ecommerce.mapper.MapperProducto;
import com.tpo.ecommerce.repository.DescuentoRepository;
import com.tpo.ecommerce.repository.ProductoRepository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@AllArgsConstructor
@Service
public class ProductoService implements IProductoService {
    private final ProductoRepository productoRepository;
    private final DescuentoRepository descuentoRepository;
    private final MapperProducto mapperProducto;

    @Override
    public ProductoDTO crear(ProductoDTO dto) {
        validarData(dto);
        Producto producto = mapperProducto.toEntity(dto);
        return mapperProducto.toDto(productoRepository.save(producto));
    }

    @Override
    public List<ProductoDTO> getProductos(Long id, Long usuarioId, String titulo, String descripcion, Double precio,
                                          Categorias categoria, Marca marca, Talle talle, Color color, Estado estado) {
        List<Producto> productos = (usuarioId != null)
                ? productoRepository.findByUsuarioId(usuarioId)
                : productoRepository.findAll();
        productos = productos.stream()
                .filter(this::estaActivo)
                .filter(p -> p.getEstadoProducto() == EstadoProducto.DISPONIBLE)
                .toList();

        if (id != null) productos = productos.stream().filter(p -> p.getId() != null && p.getId().equals(id)).toList();
        if (StringUtils.hasText(titulo)) {
            productos = productos.stream().filter(p -> p.getTitulo() != null && p.getTitulo().equalsIgnoreCase(titulo)).toList();
        }
        if (StringUtils.hasText(descripcion)) {
            productos = productos.stream().filter(p -> p.getDescripcion() != null && p.getDescripcion().equalsIgnoreCase(descripcion)).toList();
        }
        if (precio != null) {
            productos = productos.stream().filter(p -> p.getPrecio() != null && p.getPrecio().equals(precio)).toList();
        }
        if (categoria != null) productos = productos.stream().filter(p -> categoria.equals(p.getCategoria())).toList();
        if (marca != null) productos = productos.stream().filter(p -> marca.equals(p.getMarca())).toList();
        if (talle != null) productos = productos.stream().filter(p -> talle.equals(p.getTalle())).toList();
        if (color != null) productos = productos.stream().filter(p -> color.equals(p.getColor())).toList();
        if (estado != null) productos = productos.stream().filter(p -> estado.equals(p.getEstado())).toList();

        return productos.stream().map(mapperProducto::toDto).toList();
    }

    @Override
    public ProductoDTO actualizar(Long id, String titulo, String descripcion, Double precio, Categorias categoria,
                                  Marca marca, Talle talle, Color color, Estado estado, String imagenUrl) {
        if (!productoRepository.existsById(id)) {
            throw new NotFoundException("Producto no encontrado por id");
        }

        Producto producto = productoRepository.findById(id).get();
        if (!estaActivo(producto)) {
            throw new NotFoundException("Producto no encontrado por id");
        }

        if (StringUtils.hasText(titulo)) producto.setTitulo(titulo);
        if (StringUtils.hasText(descripcion)) producto.setDescripcion(descripcion);
        if (precio != null) producto.setPrecio(precio);
        if (categoria != null) producto.setCategoria(categoria);
        if (marca != null) producto.setMarca(marca);
        if (talle != null) producto.setTalle(talle);
        if (color != null) producto.setColor(color);
        if (estado != null) producto.setEstado(estado);
        if (StringUtils.hasText(imagenUrl)) producto.setImagenUrl(imagenUrl);

        return mapperProducto.toDto(productoRepository.save(producto));
    }

    @Override
    public void deleteProducto(Long id) {
        productoRepository.deleteById(id);
    }

    @Override
    public void eliminarLogico(Long id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Producto no encontrado por id"));
        producto.setEstadoRegistro(EstadoRegistro.ELIMINADO);
        productoRepository.save(producto);
    }

    @Override
    @Transactional
    public ProductoDTO aplicarDescuento(Long productoId, Long descuentoId, Long vendedorId) {
        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new NotFoundException("Producto no encontrado"));
        if (!estaActivo(producto)) {
            throw new NotFoundException("Producto no encontrado");
        }

        if (producto.getUsuario() == null || !producto.getUsuario().getId().equals(vendedorId)) {
            throw new UnauthorizedException("Solo el vendedor del producto puede aplicar un descuento");
        }

        Descuento descuento = descuentoRepository.findById(descuentoId)
                .orElseThrow(() -> new NotFoundException("Descuento no encontrado"));
        if (!estaActivo(descuento)) {
            throw new NotFoundException("Descuento no encontrado");
        }

        LocalDate hoy = LocalDate.now();
        if (hoy.isBefore(descuento.getValidoDesde()) || hoy.isAfter(descuento.getValidoHasta())) {
            throw new BadRequestException("El descuento no está vigente");
        }

        producto.setDescuento(descuento);
        return mapperProducto.toDto(productoRepository.save(producto));
    }

    @Override
    @Transactional
    public ProductoDTO removerDescuento(Long productoId, Long vendedorId) {
        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new NotFoundException("Producto no encontrado"));
        if (!estaActivo(producto)) {
            throw new NotFoundException("Producto no encontrado");
        }

        if (producto.getUsuario() == null || !producto.getUsuario().getId().equals(vendedorId)) {
            throw new UnauthorizedException("Solo el vendedor del producto puede remover el descuento");
        }

        producto.setDescuento(null);
        return mapperProducto.toDto(productoRepository.save(producto));
    }

    private void validarData(ProductoDTO dto) {
        if (dto == null || !StringUtils.hasText(dto.getTitulo()) || dto.getPrecio() == null || dto.getUsuarioId() == null) {
            throw new BadRequestException("Titulo, precio y vendedor son obligatorios");
        }
    }

    private boolean estaActivo(Producto producto) {
        return producto.getEstadoRegistro() == null || producto.getEstadoRegistro() == EstadoRegistro.ACTIVO;
    }

    private boolean estaActivo(Descuento descuento) {
        return descuento.getEstadoRegistro() == null || descuento.getEstadoRegistro() == EstadoRegistro.ACTIVO;
    }
}
