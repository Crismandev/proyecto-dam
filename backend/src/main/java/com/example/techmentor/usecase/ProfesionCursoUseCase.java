package com.example.techmentor.usecase;

import java.util.List;
import com.example.techmentor.bean.dto.ProfesionCursoDto;

/** Casos de uso de relación puesto-curso: operaciones que expone la capa de negocio. */
public interface ProfesionCursoUseCase {

    /** Lista todos los registros. */
    List<ProfesionCursoDto> listar();

    /** Lista los registros de un(a) profesion. */
    List<ProfesionCursoDto> listarPorProfesion(Long idProfesion);

    /** Obtiene un registro por id (404 si no existe). */
    ProfesionCursoDto obtener(Long id);

    /** Crea un registro nuevo. */
    ProfesionCursoDto crear(ProfesionCursoDto dto);

    /** Actualiza un registro (los campos null no se modifican). */
    ProfesionCursoDto actualizar(Long id, ProfesionCursoDto dto);

    /** Elimina un registro (409 si tiene datos relacionados). */
    void eliminar(Long id);
}
