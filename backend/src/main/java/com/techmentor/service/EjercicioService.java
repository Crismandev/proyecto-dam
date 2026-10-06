package com.techmentor.service;

import com.techmentor.dto.EjercicioDTO;
import com.techmentor.dto.EjercicioSaveDTO;
import com.techmentor.dto.OpcionEjercicioDTO;
import com.techmentor.entity.Ejercicio;
import com.techmentor.entity.Leccion;
import com.techmentor.entity.OpcionEjercicio;
import com.techmentor.exception.BadRequestException;
import com.techmentor.exception.ResourceNotFoundException;
import com.techmentor.mapper.EjercicioMapper;
import com.techmentor.repository.EjercicioRepository;
import com.techmentor.repository.LeccionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EjercicioService {

    private final EjercicioRepository ejercicioRepository;
    private final LeccionRepository leccionRepository;
    private final EjercicioMapper ejercicioMapper;

    @Transactional(readOnly = true)
    public Page<EjercicioDTO> listar(Long idLeccion, String q, Pageable pageable) {
        String query = q != null ? q.trim() : "";
        return ejercicioRepository.buscar(idLeccion, query, pageable).map(ejercicioMapper::toDTO);
    }

    @Transactional(readOnly = true)
    public EjercicioDTO obtenerPorId(Long id) {
        Ejercicio entity = ejercicioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ejercicio no encontrado con ID: " + id));
        return ejercicioMapper.toDTO(entity);
    }

    @Transactional
    public EjercicioDTO crear(EjercicioSaveDTO dto) {
        Leccion leccion = leccionRepository.findById(dto.getIdLeccion())
                .orElseThrow(() -> new ResourceNotFoundException("Lección no encontrada con ID: " + dto.getIdLeccion()));

        validarOpciones(dto);

        Ejercicio entity = new Ejercicio();
        entity.setLeccion(leccion);
        entity.setTipoEjercicio(dto.getTipoEjercicio());
        entity.setEnunciado(dto.getEnunciado());
        entity.setOrden(dto.getOrden());
        entity.setExplicacion(dto.getExplicacion());

        prepararOpcionesYRespuesta(dto, entity);

        Ejercicio guardado = ejercicioRepository.save(entity);
        return ejercicioMapper.toDTO(guardado);
    }

    @Transactional
    public EjercicioDTO actualizar(Long id, EjercicioSaveDTO dto) {
        Ejercicio entity = ejercicioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ejercicio no encontrado con ID: " + id));

        Leccion leccion = leccionRepository.findById(dto.getIdLeccion())
                .orElseThrow(() -> new ResourceNotFoundException("Lección no encontrada con ID: " + dto.getIdLeccion()));

        validarOpciones(dto);

        entity.setLeccion(leccion);
        entity.setTipoEjercicio(dto.getTipoEjercicio());
        entity.setEnunciado(dto.getEnunciado());
        entity.setOrden(dto.getOrden());
        entity.setExplicacion(dto.getExplicacion());

        entity.getOpciones().clear();
        prepararOpcionesYRespuesta(dto, entity);

        Ejercicio actualizado = ejercicioRepository.save(entity);
        return ejercicioMapper.toDTO(actualizado);
    }

    @Transactional
    public void eliminar(Long id) {
        if (!ejercicioRepository.existsById(id)) {
            throw new ResourceNotFoundException("Ejercicio no encontrado con ID: " + id);
        }
        ejercicioRepository.deleteById(id);
    }

    private void validarOpciones(EjercicioSaveDTO dto) {
        if ("OPCION_MULTIPLE".equalsIgnoreCase(dto.getTipoEjercicio()) || "MULTIPLE_CHOICE".equalsIgnoreCase(dto.getTipoEjercicio())) {
            if (dto.getOpciones() == null || dto.getOpciones().size() < 2 || dto.getOpciones().size() > 4) {
                throw new BadRequestException("Los ejercicios de opción múltiple deben tener entre 2 y 4 opciones.");
            }
            long correctas = dto.getOpciones().stream().filter(op -> Boolean.TRUE.equals(op.getEsCorrecta())).count();
            if (correctas != 1) {
                throw new BadRequestException("Debe haber exactamente una opción correcta.");
            }
        }
    }

    private void prepararOpcionesYRespuesta(EjercicioSaveDTO dto, Ejercicio entity) {
        if ("OPCION_MULTIPLE".equalsIgnoreCase(dto.getTipoEjercicio()) || "MULTIPLE_CHOICE".equalsIgnoreCase(dto.getTipoEjercicio())) {
            int ordenIdx = 1;
            for (OpcionEjercicioDTO opDto : dto.getOpciones()) {
                OpcionEjercicio op = new OpcionEjercicio();
                op.setEjercicio(entity);
                op.setTexto(opDto.getTexto() != null ? opDto.getTexto() : opDto.getTextoOpcion());
                op.setEsCorrecta(Boolean.TRUE.equals(opDto.getEsCorrecta()));
                op.setOrden(opDto.getOrden() != null ? opDto.getOrden() : ordenIdx++);
                entity.getOpciones().add(op);
                
                if (Boolean.TRUE.equals(op.getEsCorrecta())) {
                    entity.setRespuestaCorrecta(op.getTexto());
                }
            }
        } else if ("ENTREVISTA".equalsIgnoreCase(dto.getTipoEjercicio())) {
            entity.setRespuestaCorrecta(null);
        }
    }
}
