package com.example.techmentor.model;

import java.time.OffsetDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import com.example.techmentor.bean.dto.ProgresoCursoDto;
import com.example.techmentor.bean.entity.EstadoProgreso;
import com.example.techmentor.bean.entity.ProgresoCurso;
import com.example.techmentor.bean.mapper.ProgresoCursoMapper;
import com.example.techmentor.persistence.ProgresoCursoRepository;
import com.example.techmentor.security.UsuarioAutenticado;
import com.example.techmentor.usecase.ProgresoCursoUseCase;

/** Lógica de negocio de progreso curso. */
@Service
@RequiredArgsConstructor
public class ProgresoCursoModel implements ProgresoCursoUseCase {

    private final ProgresoCursoRepository progresoCursoRepository;
    private final ProgresoCursoGestor progresoCursoGestor;
    private final ProgresoCursoMapper progresoCursoMapper;

    /** Idempotente: si ya existe devuelve el progreso (y reanuda si estaba abandonado). */
    @Override
    @Transactional
    public ProgresoCursoDto iniciar(Long idCurso, UsuarioAutenticado usuario) {
        return progresoCursoMapper.toDto(progresoCursoGestor.iniciarOReanudar(usuario.idUsuario(), idCurso));
    }

    @Override
    @Transactional
    public ProgresoCursoDto abandonar(Long idCurso, UsuarioAutenticado usuario) {
        ProgresoCurso progreso = buscarDeUsuario(usuario.idUsuario(), idCurso);
        if (progreso.getEstado() == EstadoProgreso.COMPLETADO) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El curso ya está completado");
        }
        progreso.setEstado(EstadoProgreso.ABANDONADO);
        progreso.setFechaUltimaActividad(OffsetDateTime.now());
        return progresoCursoMapper.toDto(progresoCursoRepository.saveAndFlush(progreso));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProgresoCursoDto> misCursos(UsuarioAutenticado usuario) {
        return progresoCursoRepository.listarPorUsuario(usuario.idUsuario())
                .stream()
                .map(progresoCursoMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ProgresoCursoDto miProgreso(Long idCurso, UsuarioAutenticado usuario) {
        return progresoCursoMapper.toDto(buscarDeUsuario(usuario.idUsuario(), idCurso));
    }

    @Override
    @Transactional(readOnly = true)
    public ProgresoCursoDto obtener(Long id, UsuarioAutenticado usuario) {
        ProgresoCurso progreso = buscar(id);
        usuario.exigirPropietarioOStaff(progreso.getUsuario().getIdUsuario());
        return progresoCursoMapper.toDto(progreso);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProgresoCursoDto> listarPorUsuario(Long idUsuario) {
        return progresoCursoRepository.listarPorUsuario(idUsuario)
                .stream()
                .map(progresoCursoMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProgresoCursoDto> listarPorCurso(Long idCurso) {
        return progresoCursoRepository.findByCurso_IdCurso(idCurso)
                .stream()
                .map(progresoCursoMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        progresoCursoRepository.delete(buscar(id));
        progresoCursoRepository.flush();
    }

    private ProgresoCurso buscar(Long id) {
        return progresoCursoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Progreso de curso no encontrado: " + id));
    }

    private ProgresoCurso buscarDeUsuario(Long idUsuario, Long idCurso) {
        return progresoCursoRepository.findByUsuario_IdUsuarioAndCurso_IdCurso(idUsuario, idCurso)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Aún no has iniciado el curso " + idCurso));
    }
}
