package com.tpo.ecommerce.service;

import com.tpo.ecommerce.dto.ReseniaDTO;
import com.tpo.ecommerce.entity.Orden;
import com.tpo.ecommerce.entity.Producto;
import com.tpo.ecommerce.entity.Resenia;
import com.tpo.ecommerce.entity.Usuario;
import com.tpo.ecommerce.exceptions.BadRequestException;
import com.tpo.ecommerce.exceptions.DuplicateResourceException;
import com.tpo.ecommerce.exceptions.NotFoundException;
import com.tpo.ecommerce.mapper.MapperResenia;
import com.tpo.ecommerce.repository.OrdenRepository;
import com.tpo.ecommerce.repository.ProductoRepository;
import com.tpo.ecommerce.repository.ReseniaRepository;
import com.tpo.ecommerce.repository.UsuarioRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class ReseniaService implements IReseniaService {

    private final ReseniaRepository reseniaRepository;
    private final UsuarioRepository usuarioRepository;
    private final ProductoRepository productoRepository;
    private final OrdenRepository ordenRepository;
    private final MapperResenia mapperResenia;

    @Override
    @Transactional
    public ReseniaDTO crear(ReseniaDTO dto) {
        validarData(dto);

        Usuario comprador = usuarioRepository.findById(dto.getCompradorId())
                .orElseThrow(() -> new NotFoundException("Comprador no encontrado"));
        Producto producto = productoRepository.findById(dto.getProductoId())
                .orElseThrow(() -> new NotFoundException("Producto no encontrado"));
        Orden orden = ordenRepository.findById(dto.getOrdenId())
                .orElseThrow(() -> new NotFoundException("Orden no encontrada"));

        if (!ordenRepository.existsByIdAndCompradorId(dto.getOrdenId(), dto.getCompradorId())) {
            throw new BadRequestException("La orden indicada no pertenece al comprador");
        }
        if (!ordenRepository.existsCompraDeProductoEnOrden(
                dto.getOrdenId(),
                dto.getCompradorId(),
                dto.getProductoId()
        )) {
            throw new BadRequestException("El comprador no compro ese producto en la orden indicada");
        }
        if (reseniaRepository.existsByCompradorIdAndProductoId( //validar que no ponga resenia 2 veces
                dto.getCompradorId(),
                dto.getProductoId()
        )) {
            throw new DuplicateResourceException(
                    "Resenia",
                    "compradorId+productoId",
                    dto.getCompradorId() + "-" + dto.getProductoId()
            );
        }

        Usuario vendedor = producto.getUsuario();
        if (vendedor == null) {
            throw new BadRequestException("El producto no tiene vendedor asociado");
        }
        if (dto.getVendedorId() != null && !dto.getVendedorId().equals(vendedor.getId())) {
            throw new BadRequestException("El vendedorId no coincide con el vendedor del producto");
        }

        Resenia resenia = new Resenia(
                null,
                producto,
                comprador,
                vendedor,
                orden,
                dto.getCalificacion(),
                StringUtils.hasText(dto.getComentarios()) ? dto.getComentarios().trim() : null,
                LocalDateTime.now(),
                true
        );

        return mapperResenia.toDto(reseniaRepository.save(resenia));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReseniaDTO> getByProducto(Long productoId) {
        if (productoId == null) {
            throw new BadRequestException("Debe indicar productoId");
        }
        if (!productoRepository.existsById(productoId)) {
            throw new NotFoundException("Producto no encontrado");
        }
        return reseniaRepository.findByProductoIdOrderByFechaDesc(productoId).stream()
                .map(mapperResenia::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReseniaDTO> getByVendedor(Long vendedorId) {
        if (vendedorId == null) {
            throw new BadRequestException("Debe indicar vendedorId");
        }
        if (!usuarioRepository.existsById(vendedorId)) {
            throw new NotFoundException("Vendedor no encontrado");
        }
        return reseniaRepository.findByVendedorIdOrderByFechaDesc(vendedorId).stream()
                .map(mapperResenia::toDto)
                .toList();
    }

    private void validarData(ReseniaDTO dto) {
        if (dto == null) {
            throw new BadRequestException("Debe enviar datos de la resenia");
        }
        if (dto.getProductoId() == null || dto.getCompradorId() == null || dto.getOrdenId() == null) {
            throw new BadRequestException("productoId, compradorId y ordenId son obligatorios");
        }
        if (dto.getCalificacion() == null || dto.getCalificacion() < 1 || dto.getCalificacion() > 5) {
            throw new BadRequestException("La calificacion debe estar entre 1 y 5");
        }
    }
}
