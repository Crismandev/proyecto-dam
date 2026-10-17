package com.example.techmentor.usecase;

import java.util.List;
import com.example.techmentor.bean.dto.CategoriaPreguntaCursoDto;

/** Casos de uso de relación categoría de pregunta-curso: operaciones que expone la capa de negocio. */
public interface CategoriaPreguntaCursoUseCase {

    /** Lista todos los registros. */
    List<CategoriaPreguntaCursoDto> listar();

    /** Lista los registros de un(a) categoriaPregunta. */
    List<CategoriaPreguntaCursoDto> listarPorCategoriaPregunta(Long idCategoriaPregunta);

    /** Obtiene un registro por id (404 si no existe). */
    CategoriaPreguntaCursoDto obtener(Long id);

    /** Crea un registro nuevo. */
    CategoriaPreguntaCursoDto crear(CategoriaPreguntaCursoDto dto);

    /** Actualiza un registro (los campos null no se modifican). */
    CategoriaPreguntaCursoDto actualizar(Long id, CategoriaPreguntaCursoDto dto);

    /** Elimina un registro (409 si tiene datos relacionados). */
    void eliminar(Long id);
}
