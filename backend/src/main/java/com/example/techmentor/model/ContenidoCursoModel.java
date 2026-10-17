package com.example.techmentor.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import com.example.techmentor.bean.dto.*;
import com.example.techmentor.bean.entity.*;
import com.example.techmentor.bean.mapper.RecursoLeccionMapper;
import com.example.techmentor.persistence.*;
import com.example.techmentor.security.UsuarioAutenticado;
import com.example.techmentor.usecase.ContenidoCursoUseCase;

/**
 * Arma la pantalla de un curso: niveles > lecciones > (recursos activos + ejercicios sin respuesta)
 * e indica el estado de cada lección para el usuario que consulta.
 */
@Service
@RequiredArgsConstructor
public class ContenidoCursoModel implements ContenidoCursoUseCase {

    private final CursoRepository cursoRepository;
    private final NivelRepository nivelRepository;
    private final LeccionRepository leccionRepository;
    private final RecursoLeccionRepository recursoLeccionRepository;
    private final EjercicioRepository ejercicioRepository;
    private final OpcionEjercicioRepository opcionEjercicioRepository;
    private final ProgresoLeccionRepository progresoLeccionRepository;
    private final RecursoLeccionMapper recursoLeccionMapper;

    @Override
    @Transactional(readOnly = true)
    public ContenidoCursoDto obtener(Long idCurso, UsuarioAutenticado usuario) {
        Curso curso = cursoRepository.findById(idCurso).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Curso no encontrado: " + idCurso));
        if (!usuario.esStaff() && !Boolean.TRUE.equals(curso.getActivo())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Curso no encontrado: " + idCurso);
        }

        Map<Long, String> estados = new HashMap<>();
        for (ProgresoLeccion p : progresoLeccionRepository.listarPorUsuarioYCurso(usuario.idUsuario(), idCurso)) {
            estados.put(p.getLeccion().getIdLeccion(), p.getEstado().name());
        }

        List<NivelContenidoDto> niveles = new ArrayList<>();
        for (Nivel nivel : nivelRepository.findByCurso_IdCursoOrderByOrdenAsc(idCurso)) {
            List<LeccionContenidoDto> lecciones = new ArrayList<>();
            for (Leccion leccion : leccionRepository.findByNivel_IdNivelOrderByOrdenAsc(nivel.getIdNivel())) {
                lecciones.add(construirLeccion(leccion, estados.getOrDefault(leccion.getIdLeccion(), "NO_INICIADO")));
            }
            niveles.add(NivelContenidoDto.builder()
                    .idNivel(nivel.getIdNivel()).nombre(nivel.getNombre())
                    .descripcion(nivel.getDescripcion()).orden(nivel.getOrden())
                    .lecciones(lecciones).build());
        }

        return ContenidoCursoDto.builder()
                .idCurso(curso.getIdCurso()).nombre(curso.getNombre())
                .descripcion(curso.getDescripcion()).xpRecompensa(curso.getXpRecompensa())
                .niveles(niveles).build();
    }

    private LeccionContenidoDto construirLeccion(Leccion leccion, String estado) {
        List<RecursoLeccionDto> recursos = recursoLeccionRepository
                .findByLeccion_IdLeccionAndActivoTrueOrderByOrdenAsc(leccion.getIdLeccion())
                .stream().map(recursoLeccionMapper::toDto).toList();

        List<EjercicioPublicoDto> ejercicios = new ArrayList<>();
        for (Ejercicio e : ejercicioRepository.findByLeccion_IdLeccionOrderByOrdenAsc(leccion.getIdLeccion())) {
            List<OpcionEjercicioPublicaDto> opciones = opcionEjercicioRepository
                    .findByEjercicio_IdEjercicioOrderByOrdenAsc(e.getIdEjercicio()).stream()
                    .map(o -> OpcionEjercicioPublicaDto.builder()
                            .idOpcion(o.getIdOpcion()).texto(o.getTexto()).orden(o.getOrden()).build())
                    .toList();
            ejercicios.add(EjercicioPublicoDto.builder()
                    .idEjercicio(e.getIdEjercicio()).tipoEjercicio(e.getTipoEjercicio())
                    .enunciado(e.getEnunciado()).orden(e.getOrden()).xpRecompensa(e.getXpRecompensa())
                    .opciones(opciones).build());
        }

        return LeccionContenidoDto.builder()
                .idLeccion(leccion.getIdLeccion()).titulo(leccion.getTitulo())
                .descripcion(leccion.getDescripcion()).contenido(leccion.getContenido())
                .orden(leccion.getOrden()).xpRecompensa(leccion.getXpRecompensa())
                .estado(estado).recursos(recursos).ejercicios(ejercicios).build();
    }
}
