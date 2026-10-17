package com.example.techmentor.model;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import com.example.techmentor.bean.dto.CompletarLeccionDto;
import com.example.techmentor.bean.dto.ProgresoLeccionDto;
import com.example.techmentor.bean.dto.RespuestaLeccionDto;
import com.example.techmentor.bean.entity.Ejercicio;
import com.example.techmentor.bean.entity.EstadoProgreso;
import com.example.techmentor.bean.entity.Leccion;
import com.example.techmentor.bean.entity.ProgresoLeccion;
import com.example.techmentor.bean.entity.OpcionEjercicio;
import com.example.techmentor.bean.entity.Usuario;
import com.example.techmentor.bean.mapper.ProgresoLeccionMapper;
import com.example.techmentor.persistence.LeccionRepository;
import com.example.techmentor.persistence.EjercicioRepository;
import com.example.techmentor.persistence.OpcionEjercicioRepository;
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
    private final EjercicioRepository ejercicioRepository;
    private final OpcionEjercicioRepository opcionEjercicioRepository;

    /** Inicia la lección (e inicia el curso si aún no estaba iniciado). Idempotente. */
    @Override
    @Transactional
    public ProgresoLeccionDto iniciar(Long idLeccion, UsuarioAutenticado usuario) {
        Leccion leccion = buscarLeccion(idLeccion);
        bloquearUsuario(usuario.idUsuario());
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
    /* Versión anterior conservada: aceptaba el puntaje enviado por el cliente
       y solo registraba los XP en el progreso, sin sumarlos al perfil.
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
    */

    @Override
    @Transactional
    public ProgresoLeccionDto completar(Long idLeccion, CompletarLeccionDto dto, UsuarioAutenticado usuario) {
        Leccion leccion = buscarLeccion(idLeccion);
        if (!Boolean.TRUE.equals(leccion.getNivel().getCurso().getActivo())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El curso no está activo");
        }
        int puntaje = calcularPuntaje(idLeccion, dto);
        Usuario alumno = bloquearUsuario(usuario.idUsuario());
        OffsetDateTime ahora = OffsetDateTime.now();

        ProgresoLeccion progreso = obtenerOCrear(usuario.idUsuario(), leccion);
        boolean primeraVez = progreso.getEstado() != EstadoProgreso.COMPLETADO;
        progreso.setIntentos(progreso.getIntentos() + 1);
        progreso.setFechaUltimaActividad(ahora);
        if (primeraVez) {
            progreso.setEstado(EstadoProgreso.COMPLETADO);
            progreso.setPuntaje(puntaje);
            progreso.setXpGanado(leccion.getXpRecompensa());
            progreso.setFechaCompletado(ahora);
            if (progreso.getFechaInicio() == null) {
                progreso.setFechaInicio(ahora);
            }
        } else {
            progreso.setPuntaje(Math.max(progreso.getPuntaje(), puntaje));
        }

        ProgresoLeccion guardado = progresoLeccionRepository.saveAndFlush(progreso);
        if (primeraVez) {
            alumno.setXpTotal(alumno.getXpTotal() + leccion.getXpRecompensa());
            usuarioRepository.save(alumno);
            progresoCursoGestor.registrarLeccionCompletada(usuario.idUsuario(), leccion, leccion.getXpRecompensa());
        }
        return progresoLeccionMapper.toDto(guardado);
    }

    /** Valida pertenencia y respuestas completas antes de guardar progreso o premios. */
    private int calcularPuntaje(Long idLeccion, CompletarLeccionDto dto) {
        List<Ejercicio> ejercicios = ejercicioRepository.findByLeccion_IdLeccionOrderByOrdenAsc(idLeccion);
        if (dto == null || dto.getRespuestas() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Debes enviar las respuestas de la lección");
        }
        Map<Long, RespuestaLeccionDto> respuestas = new HashMap<>();
        for (RespuestaLeccionDto respuesta : dto.getRespuestas()) {
            if (respuesta == null || respuesta.getIdEjercicio() == null
                    || respuestas.putIfAbsent(respuesta.getIdEjercicio(), respuesta) != null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Hay respuestas inválidas o ejercicios repetidos");
            }
        }
        if (respuestas.size() != ejercicios.size()
                || ejercicios.stream().anyMatch(e -> !respuestas.containsKey(e.getIdEjercicio()))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Debes responder una vez cada ejercicio de esta lección");
        }

        Map<Long, List<OpcionEjercicio>> opciones = new HashMap<>();
        for (OpcionEjercicio opcion : opcionEjercicioRepository.findAllByLeccion(idLeccion)) {
            opciones.computeIfAbsent(opcion.getEjercicio().getIdEjercicio(), id -> new java.util.ArrayList<>()).add(opcion);
        }
        int puntaje = 0;
        for (Ejercicio ejercicio : ejercicios) {
            RespuestaLeccionDto respuesta = respuestas.get(ejercicio.getIdEjercicio());
            List<OpcionEjercicio> alternativas = opciones.getOrDefault(ejercicio.getIdEjercicio(), List.of());
            if (!alternativas.isEmpty()) {
                if (alternativas.stream().filter(o -> Boolean.TRUE.equals(o.getEsCorrecta())).count() != 1) {
                    throw new ResponseStatusException(HttpStatus.CONFLICT,
                            "El ejercicio " + ejercicio.getIdEjercicio() + " debe tener una opción correcta");
                }
                if (respuesta.getIdOpcionSeleccionada() == null || respuesta.getRespuestaTexto() != null) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Selecciona una opción por ejercicio");
                }
                OpcionEjercicio seleccionada = alternativas.stream()
                        .filter(o -> o.getIdOpcion().equals(respuesta.getIdOpcionSeleccionada()))
                        .findFirst().orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                "La opción no pertenece al ejercicio " + ejercicio.getIdEjercicio()));
                if (Boolean.TRUE.equals(seleccionada.getEsCorrecta())) {
                    puntaje++;
                }
            } else {
                if (ejercicio.getRespuestaCorrecta() == null || ejercicio.getRespuestaCorrecta().isBlank()) {
                    throw new ResponseStatusException(HttpStatus.CONFLICT,
                            "El ejercicio " + ejercicio.getIdEjercicio() + " no tiene respuesta configurada");
                }
                if (respuesta.getIdOpcionSeleccionada() != null
                        || respuesta.getRespuestaTexto() == null || respuesta.getRespuestaTexto().isBlank()) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Envía una respuesta escrita para este ejercicio");
                }
                if (ejercicio.getRespuestaCorrecta().strip().equals(respuesta.getRespuestaTexto().strip())) {
                    puntaje++;
                }
            }
        }
        return puntaje;
    }

    private Usuario bloquearUsuario(Long idUsuario) {
        return usuarioRepository.bloquearPorId(idUsuario).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado: " + idUsuario));
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
