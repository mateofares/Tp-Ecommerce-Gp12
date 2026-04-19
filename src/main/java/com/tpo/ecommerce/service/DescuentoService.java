package com.tpo.ecommerce.service;

import com.tpo.ecommerce.dto.DescuentoDTO;
import com.tpo.ecommerce.entity.Descuento;
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
        List<Descuento> descuentos = descuentoRepository.findAll();

        if (id != null) {
            descuentos = descuentos.stream()
                    .filter(descuento -> descuento.getId().equals(id))
                    .toList();
        }

        if (codigoDescuento != null && !codigoDescuento.isEmpty()) {
            descuentos = descuentos.stream()
                    .filter(descuento -> descuento.getCodigoDescuento().equalsIgnoreCase(codigoDescuento))
                    .toList();
        }

        if (tipo != null && !tipo.isEmpty()) {
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
                descuentoDTO.getValidoHasta(),
                descuentoDTO.getMaximoDeUsos()
        );

        return mapperDescuento.toDto(descuentoRepository.save(descuento));
    }

    @Override
    public DescuentoDTO updateDescuento(Long id, DescuentoDTO descuentoDTO) {
        Descuento descuento = descuentoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Descuento no encontrado con ID: " + id));

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
            descuento.setValor(descuentoDTO.getValor());
        }

        if (descuentoDTO.getValidoDesde() != null) {
            descuento.setValidoDesde(descuentoDTO.getValidoDesde());
        }

        if (descuentoDTO.getValidoHasta() != null) {
            descuento.setValidoHasta(descuentoDTO.getValidoHasta());
        }

        if (descuentoDTO.getMaximoDeUsos() != null) {
            if (descuentoDTO.getMaximoDeUsos() < 0) {
                throw new BadRequestException("El máximo de usos no puede ser negativo");
            }
            descuento.setMaximoDeUsos(descuentoDTO.getMaximoDeUsos());
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
    public DescuentoDTO getDescuentoPorCodigo(String codigoDescuento) {
        Descuento descuento = descuentoRepository.findByCodigoDescuento(codigoDescuento)
                .orElseThrow(() -> new NotFoundException("Descuento no encontrado con código: " + codigoDescuento));

        validarDescuentoActivo(descuento);
        return mapperDescuento.toDto(descuento);
    }

    @Override
    public void incrementarUsos(Long descuentoId) {
        Descuento descuento = descuentoRepository.findById(descuentoId)
                .orElseThrow(() -> new NotFoundException("Descuento no encontrado con ID: " + descuentoId));

        if (descuento.getUsosActuales() >= descuento.getMaximoDeUsos()) {
            throw new BadRequestException("El descuento ha alcanzado el máximo de usos");
        }

        descuento.setUsosActuales(descuento.getUsosActuales() + 1);
        descuentoRepository.save(descuento);
    }

    /**
     * Calcula el monto del descuento a aplicar
     * @param monto Monto original sin descuento
     * @param descuentoId ID del descuento
     * @return Monto del descuento
     */
    public Double calcularDescuento(Double monto, Long descuentoId) {
        Descuento descuento = descuentoRepository.findById(descuentoId)
                .orElseThrow(() -> new NotFoundException("Descuento no encontrado con ID: " + descuentoId));
        
        validarDescuentoActivo(descuento);
        
        if (descuento.getTipo() == TipoDescuento.PORCENTAJE) {
            return monto * (descuento.getValor() / 100.0);
        } else {
            // MONTO: aplicar el descuento directo, pero no mayor que el monto
            return Math.min(descuento.getValor(), monto);
        }
    }

    /**
     * Calcula el precio final con descuento aplicado
     * @param monto Monto original
     * @param descuentoId ID del descuento
     * @return Precio final después del descuento
     */
    public Double calcularPrecioFinal(Double monto, Long descuentoId) {
        Double descuentoAplicado = calcularDescuento(monto, descuentoId);
        return monto - descuentoAplicado;
    }

    /**
     * Calcula el descuento pero sin validar vigencia (para visualización)
     * @param monto Monto original
     * @param descuentoId ID del descuento
     * @return Monto del descuento
     */
    public Double calcularDescuentoSinValidar(Double monto, Long descuentoId) {
        Descuento descuento = descuentoRepository.findById(descuentoId)
                .orElseThrow(() -> new NotFoundException("Descuento no encontrado con ID: " + descuentoId));
        
        if (descuento.getTipo() == TipoDescuento.PORCENTAJE) {
            return monto * (descuento.getValor() / 100.0);
        } else {
            return Math.min(descuento.getValor(), monto);
        }
    }

    /**
     * Valida si un descuento puede ser aplicado a una compra
     * @param descuentoId ID del descuento
     * @return true si es válido y aplicable
     */
    public boolean validarDescuentoAplicable(Long descuentoId) {
        try {
            Descuento descuento = descuentoRepository.findById(descuentoId)
                    .orElseThrow(() -> new NotFoundException("Descuento no encontrado con ID: " + descuentoId));
            validarDescuentoActivo(descuento);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private void validarDatos(DescuentoDTO descuentoDTO) {
        if (!StringUtils.hasText(descuentoDTO.getCodigoDescuento())) {
            throw new BadRequestException("El código del descuento es requerido");
        }
        if (descuentoDTO.getTipo() == null) {
            throw new BadRequestException("El tipo de descuento es requerido");
        }
        if (descuentoDTO.getValor() == null || descuentoDTO.getValor() <= 0) {
            throw new BadRequestException("El valor debe ser mayor a 0");
        }
        if (descuentoDTO.getValidoDesde() == null) {
            throw new BadRequestException("La fecha de inicio es requerida");
        }
        if (descuentoDTO.getValidoHasta() == null) {
            throw new BadRequestException("La fecha de fin es requerida");
        }
        if (descuentoDTO.getMaximoDeUsos() == null || descuentoDTO.getMaximoDeUsos() < 0) {
            throw new BadRequestException("El máximo de usos no puede ser negativo");
        }
    }

    private void validarCodigoUnico(String codigo, Long idExcluir) {
        var descuentoExistente = descuentoRepository.findByCodigoDescuento(codigo);
        if (descuentoExistente.isPresent() && !descuentoExistente.get().getId().equals(idExcluir)) {
            throw new DuplicateResourceException("El código de descuento ya existe: " + codigo);
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
            throw new BadRequestException("El descuento aún no es válido. Válido desde: " + descuento.getValidoDesde());
        }
        
        if (hoy.isAfter(descuento.getValidoHasta())) {
            throw new BadRequestException("El descuento ha expirado. Válido hasta: " + descuento.getValidoHasta());
        }
        
        if (descuento.getUsosActuales() >= descuento.getMaximoDeUsos()) {
            throw new BadRequestException("El descuento ha alcanzado el máximo de usos");
        }
    }
}
