package com.techmentor.service;

import com.techmentor.dto.ProgresoLeccionDTO;
import com.techmentor.exception.ResourceNotFoundException;
import com.techmentor.mapper.ProgresoLeccionMapper;
import com.techmentor.repository.ProgresoLeccionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProgresoService {

    private final ProgresoLeccionRepository progresoLeccionRepository;
    private final ProgresoLeccionMapper progresoLeccionMapper;

    @Transactional(readOnly = true)
    public Page<ProgresoLeccionDTO> listar(Long idUsuario, Long idLeccion, Pageable pageable) {
        return progresoLeccionRepository.buscar(idUsuario, idLeccion, pageable)
                .map(progresoLeccionMapper::toDTO);
    }

    @Transactional(readOnly = true)
    public ProgresoLeccionDTO obtenerPorId(Long id) {
        return progresoLeccionRepository.findById(id)
                .map(progresoLeccionMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Registro de progreso no encontrado con ID: " + id));
    }

    @Transactional
    public void eliminar(Long id) {
        if (!progresoLeccionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Registro de progreso no encontrado con ID: " + id);
        }
        progresoLeccionRepository.deleteById(id);
    }
}
