package com.tpo.ecommerce.service;

import com.tpo.ecommerce.dto.DescuentoDTO;
import com.tpo.ecommerce.entity.Descuento;
import com.tpo.ecommerce.enums.EstadoRegistro;
import com.tpo.ecommerce.enums.TipoDescuento;
import com.tpo.ecommerce.exceptions.BadRequestException;
import com.tpo.ecommerce.exceptions.DuplicateResourceException;
import com.tpo.ecommerce.exceptions.NotFoundException;
import com.tpo.ecommerce.mapper.MapperDescuento;
import com.tpo.ecommerce.repository.DescuentoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@Service
public class DescuentoService implements IDescuentoService {
    private DescuentoRepository descuentoRepository;
    private MapperDescuento mapperDescuento;

    @Override
    public List<DescuentoDTO> getDescuentos(Long id, String codigoDescuento, String tipo) {
        List<Descuento> descuentos = descuentoRepository.findAll().stream()
                .filter(this::estaActivo)
                .toList();

        if (id != null) {
            descuentos = descuentos.stream()
                    .filter(descuento -> descuento.getId().equals(id))
                    .toList();
        }
        if (StringUtils.hasText(codigoDescuento)) {
            descuentos = descuentos.stream()
                    .filter(descuento -> descuento.getCodigoDescuento().equalsIgnoreCase(codigoDescuento))
                    .toList();
        }
        if (StringUtils.hasText(tipo)) {
            descuentos = descuentos.stream()
                    .filter(descuento -> descuento.getTipo().name().equalsIgnoreCase(tipo))
                    .toList();
        }

        return descuentos.stream()
                .map(mapperDescuento::toDto)
                .toList();
    }

    @Override
    public DescuentoDTO createDescuento(DescuentoDTO descuentoDTO) {
        validarDatos(descuentoDTO);
        validarCodigoUnico(descuentoDTO.getCodigoDescuento(), null);
        validarFechas(descuentoDTO.getValidoDesde(), descuentoDTO.getValidoHasta());

        Descuento descuento = new Descuento(
                descuentoDTO.getCodigoDescuento().trim(),
                descuentoDTO.getTipo(),
                descuentoDTO.getValor(),
                descuentoDTO.getValidoDesde(),
                descuentoDTO.getValidoHasta()
        );

        return mapperDescuento.toDto(descuentoRepository.save(descuento));
    }

    @Override
    public DescuentoDTO updateDescuento(Long id, DescuentoDTO descuentoDTO) {
        Descuento descuento = obtenerDescuentoActivoPorId(id);

        if (StringUtils.hasText(descuentoDTO.getCodigoDescuento())) {
            validarCodigoUnico(descuentoDTO.getCodigoDescuento(), id);
            descuento.setCodigoDescuento(descuentoDTO.getCodigoDescuento().trim());
        }
        if (descuentoDTO.getTipo() != null) {
            descuento.setTipo(descuentoDTO.getTipo());
        }
        if (descuentoDTO.getValor() != null) {
            if (descuentoDTO.getValor() <= 0) {
                throw new BadRequestException("El valor debe ser mayor a 0");
            }
            TipoDescuento tipoFinal = descuentoDTO.getTipo() != null ? descuentoDTO.getTipo() : descuento.getTipo();
            if (tipoFinal == TipoDescuento.PORCENTAJE && descuentoDTO.getValor() > 100) {
                throw new BadRequestException("El porcentaje de descuento no puede ser mayor a 100");
            }
            descuento.setValor(descuentoDTO.getValor());
        }
        if (descuentoDTO.getValidoDesde() != null) {
            descuento.setValidoDesde(descuentoDTO.getValidoDesde());
        }
        if (descuentoDTO.getValidoHasta() != null) {
            descuento.setValidoHasta(descuentoDTO.getValidoHasta());
        }
        if (descuento.getValidoDesde() != null && descuento.getValidoHasta() != null) {
            validarFechas(descuento.getValidoDesde(), descuento.getValidoHasta());
        }

        return mapperDescuento.toDto(descuentoRepository.save(descuento));
    }

    @Override
    public void deleteDescuento(Long id) {
        Descuento descuento = descuentoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Descuento no encontrado con ID: " + id));
        descuentoRepository.delete(descuento);
    }

    @Override
    public void eliminarLogico(Long id) {
        Descuento descuento = descuentoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Descuento no encontrado con ID: " + id));
        descuento.setEstadoRegistro(EstadoRegistro.ELIMINADO);
        descuentoRepository.save(descuento);
    }

    @Override
    public DescuentoDTO getDescuentoPorCodigo(String codigoDescuento) {
        Descuento descuento = descuentoRepository.findByCodigoDescuento(codigoDescuento)
                .orElseThrow(() -> new NotFoundException("Descuento no encontrado con codigo: " + codigoDescuento));
        if (!estaActivo(descuento)) {
            throw new NotFoundException("Descuento no encontrado con codigo: " + codigoDescuento);
        }
        validarDescuentoActivo(descuento);
        return mapperDescuento.toDto(descuento);
    }

    @Override
    public Double calcularDescuento(Double monto, Long descuentoId) {
        Descuento descuento = obtenerDescuentoActivoPorId(descuentoId);
        validarDescuentoActivo(descuento);
        return aplicarCalculo(monto, descuento);
    }

    @Override
    public Double calcularPrecioFinal(Double monto, Long descuentoId) {
        return monto - calcularDescuento(monto, descuentoId);
    }

    @Override
    public Double calcularDescuentoSinValidar(Double monto, Long descuentoId) {
        Descuento descuento = obtenerDescuentoActivoPorId(descuentoId);
        return aplicarCalculo(monto, descuento);
    }

    @Override
    public boolean validarDescuentoAplicable(Long descuentoId) {
        try {
            Descuento descuento = obtenerDescuentoActivoPorId(descuentoId);
            validarDescuentoActivo(descuento);
            return true;
        } catch (BadRequestException | NotFoundException e) {
            return false;
        }
    }

    private Double aplicarCalculo(Double monto, Descuento descuento) {
        if (descuento.getTipo() == TipoDescuento.PORCENTAJE) {
            return monto * (descuento.getValor() / 100.0);
        }
        return Math.min(descuento.getValor(), monto);
    }

    private void validarDatos(DescuentoDTO dto) {
        if (!StringUtils.hasText(dto.getCodigoDescuento())) {
            throw new BadRequestException("El codigo del descuento es requerido");
        }
        if (dto.getTipo() == null) {
            throw new BadRequestException("El tipo de descuento es requerido");
        }
        if (dto.getValor() == null || dto.getValor() <= 0) {
            throw new BadRequestException("El valor debe ser mayor a 0");
        }
        if (dto.getTipo() == TipoDescuento.PORCENTAJE && dto.getValor() > 100) {
            throw new BadRequestException("El porcentaje de descuento no puede ser mayor a 100");
        }
        if (dto.getValidoDesde() == null) {
            throw new BadRequestException("La fecha de inicio es requerida");
        }
        if (dto.getValidoHasta() == null) {
            throw new BadRequestException("La fecha de fin es requerida");
        }
    }

    private void validarCodigoUnico(String codigo, Long idExcluir) {
        var existente = descuentoRepository.findByCodigoDescuento(codigo);
        if (existente.isPresent() && estaActivo(existente.get()) && !existente.get().getId().equals(idExcluir)) {
            throw new DuplicateResourceException("Descuento", "codigoDescuento", codigo);
        }
    }

    private void validarFechas(LocalDate desde, LocalDate hasta) {
        if (desde.isAfter(hasta)) {
            throw new BadRequestException("La fecha de inicio no puede ser posterior a la fecha de fin");
        }
    }

    private void validarDescuentoActivo(Descuento descuento) {
        LocalDate hoy = LocalDate.now();
        if (hoy.isBefore(descuento.getValidoDesde())) {
            throw new BadRequestException("El descuento aun no es valido. Valido desde: " + descuento.getValidoDesde());
        }
        if (hoy.isAfter(descuento.getValidoHasta())) {
            throw new BadRequestException("El descuento ha expirado. Valido hasta: " + descuento.getValidoHasta());
        }
    }

    private Descuento obtenerDescuentoActivoPorId(Long id) {
        Descuento descuento = descuentoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Descuento no encontrado con ID: " + id));
        if (!estaActivo(descuento)) {
            throw new NotFoundException("Descuento no encontrado con ID: " + id);
        }
        return descuento;
    }

    private boolean estaActivo(Descuento descuento) {
        return descuento.getEstadoRegistro() == null || descuento.getEstadoRegistro() == EstadoRegistro.ACTIVO;
    }
}
