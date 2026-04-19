package com.tpo.ecommerce.service;

import com.tpo.ecommerce.dto.DireccionDTO;
import com.tpo.ecommerce.entity.Direccion;
import com.tpo.ecommerce.entity.Usuario;
import com.tpo.ecommerce.exceptions.BadRequestException;
import com.tpo.ecommerce.exceptions.NotFoundException;
import com.tpo.ecommerce.mapper.MapperDireccion;
import com.tpo.ecommerce.repository.DireccionRepository;
import com.tpo.ecommerce.repository.UsuarioRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@AllArgsConstructor
@Service
public class DireccionService implements IDireccionService {

    private final DireccionRepository direccionRepository;
    private final MapperDireccion mapperDireccion;
    private final UsuarioRepository usuarioRepository;

    @Override
    public DireccionDTO crear(DireccionDTO dto) {
        validarData(dto);
        Direccion direccion = mapperDireccion.toEntity(dto);
        direccion = direccionRepository.save(direccion);
        return mapperDireccion.toDto(direccion);
    }

    @Override
    public DireccionDTO obtener(Long id) {
        Direccion direccion = direccionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Dirección no encontrada"));
        return mapperDireccion.toDto(direccion);
    }

    @Override
    public List<DireccionDTO> obtenerPorUsuario(Long usuarioId) {
        validarUsuarioExiste(usuarioId);
        List<Direccion> direcciones = direccionRepository.findByUsuarioId(usuarioId);
        return direcciones.stream()
                .map(mapperDireccion::toDto)
                .toList();
    }

    @Override
    public List<DireccionDTO> obtenerActivas(Long usuarioId) {
        validarUsuarioExiste(usuarioId);
        List<Direccion> direcciones = direccionRepository
                .findByUsuarioIdAndActiva(usuarioId, true);
        return direcciones.stream()
                .map(mapperDireccion::toDto)
                .toList();
    }

    @Override
    public DireccionDTO actualizar(Long id, DireccionDTO dto) {
        if (!direccionRepository.existsById(id)) {
            throw new NotFoundException("Dirección no encontrada");
        }

        Direccion direccion = direccionRepository.findById(id).get();

        if (StringUtils.hasText(dto.getCalle())) {
            direccion.setCalle(dto.getCalle());
        }
        if (StringUtils.hasText(dto.getNumero())) {
            direccion.setNumero(dto.getNumero());
        }
        if (StringUtils.hasText(dto.getCiudad())) {
            direccion.setCiudad(dto.getCiudad());
        }
        if (StringUtils.hasText(dto.getCodigoPostal())) {
            direccion.setCodigoPostal(dto.getCodigoPostal());
        }
        if (StringUtils.hasText(dto.getProvincia())) {
            direccion.setProvincia(dto.getProvincia());
        }
        if (StringUtils.hasText(dto.getTipoDireccion())) {
            direccion.setTipoDireccion(dto.getTipoDireccion());
        }
        if (dto.getPredeterminada() != null) {
            direccion.setPredeterminada(dto.getPredeterminada());
        }
        if (StringUtils.hasText(dto.getNotas())) {
            direccion.setNotas(dto.getNotas());
        }
        if (dto.getActiva() != null) {
            direccion.setActiva(dto.getActiva());
        }

        direccion = direccionRepository.save(direccion);
        return mapperDireccion.toDto(direccion);
    }

    @Override
    public void eliminarLogico(Long id) {
        if (!direccionRepository.existsById(id)) {
            throw new NotFoundException("Dirección no encontrada");
        }

        Direccion direccion = direccionRepository.findById(id).get();
        direccion.setActiva(false);
        direccionRepository.save(direccion);
    }

    @Override
    public DireccionDTO establecerPredeterminada(Long id) {
        if (!direccionRepository.existsById(id)) {
            throw new NotFoundException("Dirección no encontrada");
        }

        Direccion direccion = direccionRepository.findById(id).get();
        Long usuarioId = direccion.getUsuario().getId();

        List<Direccion> todasLasDirecciones = direccionRepository
                .findAllByUsuarioIdAndPredeterminada(usuarioId, true);

        for (Direccion dir : todasLasDirecciones) {
            dir.setPredeterminada(false);
        }
        direccionRepository.saveAll(todasLasDirecciones);

        direccion.setPredeterminada(true);
        direccion = direccionRepository.save(direccion);

        return mapperDireccion.toDto(direccion);
    }

    @Override
    public DireccionDTO obtenerPredeterminada(Long usuarioId) {
        validarUsuarioExiste(usuarioId);
        Direccion direccion = direccionRepository
                .findByUsuarioIdAndPredeterminada(usuarioId, true)
                .orElse(null);

        if (direccion == null) {
            return null;
        }

        return mapperDireccion.toDto(direccion);
    }

    private void validarData(DireccionDTO dto) {
        if (dto == null) {
            throw new BadRequestException("Los datos de dirección no pueden ser nulos");
        }
        if (!StringUtils.hasText(dto.getCalle())) {
            throw new BadRequestException("La calle es obligatoria");
        }
        if (!StringUtils.hasText(dto.getNumero())) {
            throw new BadRequestException("El número es obligatorio");
        }
        if (!StringUtils.hasText(dto.getCiudad())) {
            throw new BadRequestException("La ciudad es obligatoria");
        }
        if (!StringUtils.hasText(dto.getCodigoPostal())) {
            throw new BadRequestException("El código postal es obligatorio");
        }
        if (!StringUtils.hasText(dto.getProvincia())) {
            throw new BadRequestException("La provincia es obligatoria");
        }
        if (!StringUtils.hasText(dto.getTipoDireccion())) {
            throw new BadRequestException("El tipo de direccion es obligatorio");
        }
        if (dto.getUsuarioId() == null) {
            throw new BadRequestException("El usuario es obligatorio");
        }
        validarUsuarioExiste(dto.getUsuarioId());
    }

    private void validarUsuarioExiste(Long usuarioId) {
        if (!usuarioRepository.existsById(usuarioId)) {
            throw new NotFoundException("Usuario no encontrado con id: " + usuarioId);
        }
    }
}
