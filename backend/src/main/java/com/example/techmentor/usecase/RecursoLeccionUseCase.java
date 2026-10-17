package com.example.techmentor.usecase;

import java.util.List;
import com.example.techmentor.bean.dto.RecursoLeccionDto;

/** Casos de uso de recurso de lección: operaciones que expone la capa de negocio. */
public interface RecursoLeccionUseCase {

    /** Lista todos los registros. */
    List<RecursoLeccionDto> listar();

    /** Lista los registros de un(a) leccion. */
    List<RecursoLeccionDto> listarPorLeccion(Long idLeccion);

    /** Obtiene un registro por id (404 si no existe). */
    RecursoLeccionDto obtener(Long id);

    /** Crea un registro nuevo. */
    RecursoLeccionDto crear(RecursoLeccionDto dto);

    /** Actualiza un registro (los campos null no se modifican). */
    RecursoLeccionDto actualizar(Long id, RecursoLeccionDto dto);

    /** Activa o desactiva un registro sin borrarlo. */
    RecursoLeccionDto cambiarActivo(Long id, boolean valor);

    /** Elimina un registro (409 si tiene datos relacionados). */
    void eliminar(Long id);
}
