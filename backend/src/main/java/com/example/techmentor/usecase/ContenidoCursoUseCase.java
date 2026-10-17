package com.example.techmentor.usecase;

import com.example.techmentor.bean.dto.ContenidoCursoDto;
import com.example.techmentor.security.UsuarioAutenticado;

/** Caso de uso para ver un curso completo: niveles, lecciones, recursos y ejercicios. */
public interface ContenidoCursoUseCase {

    /** Estructura del curso con el estado de cada lección para el usuario que consulta. */
    ContenidoCursoDto obtener(Long idCurso, UsuarioAutenticado usuario);
}
