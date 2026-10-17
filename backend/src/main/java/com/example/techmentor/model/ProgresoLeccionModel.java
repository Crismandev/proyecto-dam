package com.example.techmentor.model;

import java.time.OffsetDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import com.example.techmentor.bean.dto.CompletarLeccionDto;
import com.example.techmentor.bean.dto.ProgresoLeccionDto;
import com.example.techmentor.bean.entity.EstadoProgreso;
import com.example.techmentor.bean.entity.Leccion;
import com.example.techmentor.bean.entity.ProgresoLeccion;
import com.example.techmentor.bean.mapper.ProgresoLeccionMapper;
import com.example.techmentor.persistence.LeccionRepository;
import com.example.techmentor.persistence.ProgresoLeccionRepository;
import com.example.techmentor.persistence.UsuarioRepository;
import com.example.techmentor.security.UsuarioAutenticado;
import com.example.techmentor.usecase.ProgresoLeccionUseCase;

/** Lógica de negocio de progreso leccion. */
@Service
@RequiredArgsConstructor
public class ProgresoLeccionModel implements ProgresoLeccionUseCase {

    private final ProgresoLeccionRepository progresoLeccionRepository;
    private final LeccionRepository leccionRepository;
    private final UsuarioRepository usuarioRepository;
    private final ProgresoCursoGestor progresoCursoGestor;
    private final ProgresoLeccionMapper progresoLeccionMapper;

    /** Inicia la lección (e inicia el curso si aún no estaba iniciado). Idempotente. */
    @Override
    @Transactional
    public ProgresoLeccionDto iniciar(Long idLeccion, UsuarioAutenticado usuario) {
        Leccion leccion = buscarLeccion(idLeccion);
        progresoCursoGestor.iniciarOReanudar(usuario.idUsuario(), leccion.getNivel().getCurso().getIdCurso());

        OffsetDateTime ahora = OffsetDateTime.now();
        ProgresoLeccion progreso = obtenerOCrear(usuario.idUsuario(), leccion);
        if (progreso.getEstado() == EstadoProgreso.NO_INICIADO) {
            progreso.setEstado(EstadoProgreso.EN_CURSO);
        }
        if (progreso.getFechaInicio() == null) {
            progreso.setFechaInicio(ahora);
        }
        progreso.setFechaUltimaActividad(ahora);
        return progresoLeccionMapper.toDto(progresoLeccionRepository.saveAndFlush(progreso));
    }

    /**
     * Completa la lección. La primera vez otorga el XP de la lección y actualiza el curso;
     * las siguientes solo suman intento y conservan el mejor puntaje.
     */
    @Override
    @Transactional
    public ProgresoLeccionDto completar(Long idLeccion, CompletarLeccionDto dto, UsuarioAutenticado usuario) {
        Leccion leccion = buscarLeccion(idLeccion);
        OffsetDateTime ahora = OffsetDateTime.now();

        ProgresoLeccion progreso = obtenerOCrear(usuario.idUsuario(), leccion);
        boolean primeraVez = progreso.getEstado() != EstadoProgreso.COMPLETADO;

        progreso.setIntentos(progreso.getIntentos() + 1);
        progreso.setFechaUltimaActividad(ahora);
        if (primeraVez) {
            progreso.setEstado(EstadoProgreso.COMPLETADO);
            progreso.setPuntaje(dto.getPuntaje());
            progreso.setXpGanado(leccion.getXpRecompensa());
            progreso.setFechaCompletado(ahora);
            if (progreso.getFechaInicio() == null) {
                progreso.setFechaInicio(ahora);
            }
        } else {
            progreso.setPuntaje(Math.max(progreso.getPuntaje(), dto.getPuntaje()));
        }

        ProgresoLeccion guardado = progresoLeccionRepository.saveAndFlush(progreso);
        if (primeraVez) {
            progresoCursoGestor.registrarLeccionCompletada(usuario.idUsuario(), leccion, leccion.getXpRecompensa());
        }
        return progresoLeccionMapper.toDto(guardado);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProgresoLeccionDto> misLecciones(Long idCurso, UsuarioAutenticado usuario) {
        List<ProgresoLeccion> lista = (idCurso == null)
                ? progresoLeccionRepository.findByUsuario_IdUsuario(usuario.idUsuario())
                : progresoLeccionRepository.listarPorUsuarioYCurso(usuario.idUsuario(), idCurso);
        return lista.stream().map(progresoLeccionMapper::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ProgresoLeccionDto obtener(Long id, UsuarioAutenticado usuario) {
        ProgresoLeccion progreso = progresoLeccionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Progreso de lección no encontrado: " + id));
        usuario.exigirPropietarioOStaff(progreso.getUsuario().getIdUsuario());
        return progresoLeccionMapper.toDto(progreso);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProgresoLeccionDto> listarPorUsuario(Long idUsuario) {
        return progresoLeccionRepository.findByUsuario_IdUsuario(idUsuario)
                .stream()
                .map(progresoLeccionMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        ProgresoLeccion progreso = progresoLeccionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Progreso de lección no encontrado: " + id));
        progresoLeccionRepository.delete(progreso);
        progresoLeccionRepository.flush();
    }

    private Leccion buscarLeccion(Long idLeccion) {
        return leccionRepository.findById(idLeccion)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Lección no encontrada: " + idLeccion));
    }

    private ProgresoLeccion obtenerOCrear(Long idUsuario, Leccion leccion) {
        return progresoLeccionRepository
                .findByUsuario_IdUsuarioAndLeccion_IdLeccion(idUsuario, leccion.getIdLeccion())
                .orElseGet(() -> ProgresoLeccion.builder()
                        .usuario(usuarioRepository.getReferenceById(idUsuario))
                        .leccion(leccion)
                        .estado(EstadoProgreso.EN_CURSO)
                        .fechaInicio(OffsetDateTime.now())
                        .build());
    }
}
