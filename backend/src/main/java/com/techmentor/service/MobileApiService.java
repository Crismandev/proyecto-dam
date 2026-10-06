package com.techmentor.service;

import com.techmentor.dto.*;
import com.techmentor.entity.*;
import com.techmentor.exception.BadRequestException;
import com.techmentor.exception.ResourceNotFoundException;
import com.techmentor.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MobileApiService {

    private final UsuarioRepository usuarioRepository;
    private final CursoRepository cursoRepository;
    private final NivelRepository nivelRepository;
    private final LeccionRepository leccionRepository;
    private final EjercicioRepository ejercicioRepository;
    private final ProgresoLeccionRepository progresoLeccionRepository;

    @Transactional(readOnly = true)
    public MobilePerfilDTO obtenerPerfil(Long idUsuario) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        long xpTotal = progresoLeccionRepository.findByUsuarioIdUsuario(idUsuario).stream()
                .mapToLong(p -> p.getXpGanado() != null ? p.getXpGanado() : 0)
                .sum();

        int racha = usuario.getRachaDiasActual() != null ? usuario.getRachaDiasActual() : calcularRacha(idUsuario);
        int nivelActual = (int) (xpTotal / 100) + 1;
        String rango = calcularRango(nivelActual);

        return MobilePerfilDTO.builder()
                .idUsuario(usuario.getIdUsuario())
                .nombres(usuario.getNombres())
                .apellidos(usuario.getApellidos())
                .correo(usuario.getEmail())
                .experienciaTotal(xpTotal)
                .rachaDias(racha)
                .nivelActual(nivelActual)
                .rango(rango)
                .build();
    }

    @Transactional(readOnly = true)
    public List<MobileCursoDTO> listarCursosActivos(Long idUsuario) {
        List<Curso> cursos = cursoRepository.findAll();
        List<MobileCursoDTO> result = new ArrayList<>();
        
        List<ProgresoLeccion> progresos = progresoLeccionRepository.findByUsuarioIdUsuario(idUsuario);
        Set<Long> leccionesCompletadas = progresos.stream()
                .filter(p -> "COMPLETADO".equalsIgnoreCase(p.getEstado()))
                .map(p -> p.getLeccion().getIdLeccion())
                .collect(Collectors.toSet());

        for (Curso c : cursos) {
            long totalLecciones = c.getNiveles().stream()
                    .mapToLong(n -> n.getLecciones().size())
                    .sum();
            
            long completadas = c.getNiveles().stream()
                    .flatMap(n -> n.getLecciones().stream())
                    .filter(l -> leccionesCompletadas.contains(l.getIdLeccion()))
                    .count();
            
            int porcentaje = totalLecciones > 0 ? (int) ((completadas * 100) / totalLecciones) : 0;
            
            int xpDisponible = c.getNiveles().stream()
                    .flatMap(n -> n.getLecciones().stream())
                    .mapToInt(l -> l.getEjercicios().size() * 10)
                    .sum();

            result.add(MobileCursoDTO.builder()
                    .idCurso(c.getIdCurso())
                    .nombreCurso(c.getNombre())
                    .descripcion(c.getDescripcion())
                    .icono(c.getIcono())
                    .dificultad("INTERMEDIO")
                    .xpTotalDisponible(xpDisponible)
                    .porcentajeProgreso(porcentaje)
                    .bloqueado(false)
                    .build());
        }
        return result;
    }

    @Transactional(readOnly = true)
    public MobileRutaDTO obtenerRutaCurso(Long idCurso, Long idUsuario) {
        Curso curso = cursoRepository.findById(idCurso)
                .orElseThrow(() -> new ResourceNotFoundException("Curso no encontrado"));

        List<ProgresoLeccion> progresos = progresoLeccionRepository.findByUsuarioIdUsuario(idUsuario);
        Set<Long> leccionesCompletadas = progresos.stream()
                .filter(p -> "COMPLETADO".equalsIgnoreCase(p.getEstado()))
                .map(p -> p.getLeccion().getIdLeccion())
                .collect(Collectors.toSet());

        List<MobileRutaDTO.NivelRutaDTO> nivelesDto = new ArrayList<>();
        int totalLecciones = 0;
        int leccionesHechas = 0;

        for (Nivel n : curso.getNiveles()) {
            List<MobileRutaDTO.LeccionRutaDTO> leccionesDto = new ArrayList<>();
            for (Leccion l : n.getLecciones()) {
                totalLecciones++;
                boolean completada = leccionesCompletadas.contains(l.getIdLeccion());
                if (completada) leccionesHechas++;
                
                leccionesDto.add(MobileRutaDTO.LeccionRutaDTO.builder()
                        .idLeccion(l.getIdLeccion())
                        .titulo(l.getTitulo())
                        .orden(l.getOrden())
                        .completada(completada)
                        .bloqueada(false)
                        .estrellas(completada ? 3 : 0)
                        .build());
            }
            nivelesDto.add(MobileRutaDTO.NivelRutaDTO.builder()
                    .idNivel(n.getIdNivel())
                    .titulo(n.getNombreNivel())
                    .orden(n.getOrden())
                    .lecciones(leccionesDto)
                    .build());
        }

        int porcentaje = totalLecciones > 0 ? (leccionesHechas * 100) / totalLecciones : 0;

        return MobileRutaDTO.builder()
                .idCurso(curso.getIdCurso())
                .nombreCurso(curso.getNombre())
                .porcentajeTotal(porcentaje)
                .niveles(nivelesDto)
                .build();
    }

    @Transactional(readOnly = true)
    public MobileLeccionDTO obtenerDetalleLeccion(Long idLeccion) {
        Leccion leccion = leccionRepository.findById(idLeccion)
                .orElseThrow(() -> new ResourceNotFoundException("Lección no encontrada"));

        List<MobileLeccionDTO.MobileEjercicioDTO> ejerciciosDto = new ArrayList<>();
        for (Ejercicio e : leccion.getEjercicios()) {
            List<MobileLeccionDTO.MobileOpcionDTO> opcionesDto = new ArrayList<>();
            boolean esOpcionMultiple = "OPCION_MULTIPLE".equalsIgnoreCase(e.getTipoEjercicio()) || "MULTIPLE_CHOICE".equalsIgnoreCase(e.getTipoEjercicio());
            boolean esEntrevista = "ENTREVISTA".equalsIgnoreCase(e.getTipoEjercicio());

            if (esOpcionMultiple && e.getOpciones() != null) {
                for (OpcionEjercicio op : e.getOpciones()) {
                    opcionesDto.add(MobileLeccionDTO.MobileOpcionDTO.builder()
                            .idOpcion(op.getIdOpcion())
                            .texto(op.getTexto())
                            .build());
                }
            }

            ejerciciosDto.add(MobileLeccionDTO.MobileEjercicioDTO.builder()
                    .idEjercicio(e.getIdEjercicio())
                    .tipoEjercicio(e.getTipoEjercicio())
                    .enunciado(e.getEnunciado())
                    .orden(e.getOrden())
                    .opciones(esOpcionMultiple ? opcionesDto : null)
                    .idEntrevista(esEntrevista ? UUID.randomUUID().toString() : null)
                    .build());
        }

        return MobileLeccionDTO.builder()
                .idLeccion(leccion.getIdLeccion())
                .titulo(leccion.getTitulo())
                .descripcion(leccion.getContenido())
                .totalEjercicios(ejerciciosDto.size())
                .ejercicios(ejerciciosDto)
                .build();
    }

    @Transactional(readOnly = true)
    public MobileEvaluacionDTO evaluarEjercicio(Long idEjercicio, Long idOpcionSeleccionada) {
        Ejercicio ejercicio = ejercicioRepository.findById(idEjercicio)
                .orElseThrow(() -> new ResourceNotFoundException("Ejercicio no encontrado"));

        boolean esOpcionMultiple = "OPCION_MULTIPLE".equalsIgnoreCase(ejercicio.getTipoEjercicio()) || "MULTIPLE_CHOICE".equalsIgnoreCase(ejercicio.getTipoEjercicio());
        if (!esOpcionMultiple) {
            throw new BadRequestException("El ejercicio no es de opción múltiple");
        }

        OpcionEjercicio opcion = ejercicio.getOpciones().stream()
                .filter(o -> o.getIdOpcion().equals(idOpcionSeleccionada))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Opción no encontrada en este ejercicio"));

        return MobileEvaluacionDTO.builder()
                .idEjercicio(idEjercicio)
                .idOpcionSeleccionada(idOpcionSeleccionada)
                .esCorrecta(opcion.getEsCorrecta())
                .explicacion(ejercicio.getExplicacion())
                .respuestaCorrectaTexto(ejercicio.getRespuestaCorrecta())
                .build();
    }

    @Transactional(readOnly = true)
    public MobileEntrevistaDTO procesarEntrevista(Long idEjercicio, String inputUsuario) {
        Ejercicio ejercicio = ejercicioRepository.findById(idEjercicio)
                .orElseThrow(() -> new ResourceNotFoundException("Ejercicio no encontrado"));

        if (!"ENTREVISTA".equalsIgnoreCase(ejercicio.getTipoEjercicio())) {
            throw new BadRequestException("El ejercicio no es de tipo entrevista");
        }

        List<String> tips = ejercicio.getExplicacion() != null ? Arrays.asList(ejercicio.getExplicacion().split("\\n")) : Collections.emptyList();

        return MobileEntrevistaDTO.builder()
                .idEjercicio(idEjercicio)
                .inputEstudiante(inputUsuario)
                .tips(tips)
                .respuestaSugerida("Recuerda seguir estos tips al responder en una entrevista real.")
                .build();
    }

    @Transactional
    public ProgresoLeccionDTO sincronizarProgreso(Long idUsuario, ProgresoSyncDTO dto) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
                
        Leccion leccion = leccionRepository.findById(dto.getIdLeccion())
                .orElseThrow(() -> new ResourceNotFoundException("Lección no encontrada"));

        ProgresoLeccion progreso = new ProgresoLeccion();
        progreso.setUsuario(usuario);
        progreso.setLeccion(leccion);
        progreso.setEstado("COMPLETADO");
        progreso.setPuntaje(dto.getPreguntasCorrectas());
        progreso.setIntentos(1);
        progreso.setFechaInicio(OffsetDateTime.now());
        progreso.setFechaUltimaActividad(OffsetDateTime.now());
        progreso.setFechaCompletado(dto.getFechaCompletado() != null ? dto.getFechaCompletado().atOffset(ZoneOffset.UTC) : OffsetDateTime.now());
        progreso.setXpGanado(dto.getXpGanado() != null ? dto.getXpGanado() : 0);

        ProgresoLeccion guardado = progresoLeccionRepository.save(progreso);
        return mapToDto(guardado);
    }

    private ProgresoLeccionDTO mapToDto(ProgresoLeccion p) {
        return ProgresoLeccionDTO.builder()
                .idProgreso(p.getIdProgreso())
                .idUsuario(p.getUsuario().getIdUsuario())
                .idLeccion(p.getLeccion().getIdLeccion())
                .xpGanado(p.getXpGanado())
                .fechaCompletado(p.getFechaCompletado() != null ? p.getFechaCompletado().toLocalDateTime() : null)
                .build();
    }

    private int calcularRacha(Long idUsuario) {
        List<ProgresoLeccion> progresos = progresoLeccionRepository.findByUsuarioIdUsuario(idUsuario);
        if (progresos.isEmpty()) return 0;

        Set<LocalDate> diasActivos = progresos.stream()
                .filter(p -> p.getFechaCompletado() != null)
                .map(p -> p.getFechaCompletado().toLocalDate())
                .collect(Collectors.toSet());

        LocalDate hoy = LocalDate.now();
        int racha = 0;

        while (diasActivos.contains(hoy)) {
            racha++;
            hoy = hoy.minusDays(1);
        }

        if (racha == 0) {
            hoy = LocalDate.now().minusDays(1);
            while (diasActivos.contains(hoy)) {
                racha++;
                hoy = hoy.minusDays(1);
            }
        }
        
        return racha;
    }

    private String calcularRango(int nivel) {
        if (nivel < 5) return "Principiante";
        if (nivel < 10) return "Aprendiz";
        if (nivel < 20) return "Desarrollador Junior";
        if (nivel < 50) return "Desarrollador Semi-Senior";
        return "Desarrollador Senior";
    }
}
