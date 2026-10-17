package com.example.techmentor.usecase;

import java.util.List;
import com.example.techmentor.bean.dto.AnalisisCategoriaDto;
import com.example.techmentor.bean.dto.RecomendacionCursoDto;
import com.example.techmentor.security.UsuarioAutenticado;

/** Casos de uso de recomendación de cursos a partir del examen diagnóstico y el puesto del usuario. */
public interface RecomendacionUseCase {

    /** Genera (o regenera) las recomendaciones de un resultado aplicando las reglas de la BD. */
    List<RecomendacionCursoDto> generar(Long idResultado, UsuarioAutenticado solicitante);

    /** Porcentaje obtenido por categoría de pregunta en un resultado. */
    List<AnalisisCategoriaDto> analisis(Long idResultado, UsuarioAutenticado solicitante);

    /** Recomendaciones del usuario autenticado. */
    List<RecomendacionCursoDto> misRecomendaciones(Long idUsuario);

    /** Recomendaciones asociadas a un resultado. */
    List<RecomendacionCursoDto> porResultado(Long idResultado, UsuarioAutenticado solicitante);

    /** Recomendaciones de cualquier usuario (staff). */
    List<RecomendacionCursoDto> listarPorUsuario(Long idUsuario);

    /** Cambia el estado: PENDIENTE, INICIADA o DESCARTADA. */
    RecomendacionCursoDto cambiarEstado(Long id, String estado, UsuarioAutenticado solicitante);
}
