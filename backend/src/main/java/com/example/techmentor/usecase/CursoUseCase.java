package com.example.techmentor.usecase;

import java.util.List;
import com.example.techmentor.bean.dto.CursoDto;

/** Casos de uso de curso: operaciones que expone la capa de negocio. */
public interface CursoUseCase {

    /** Lista solo los registros activos (filtro opcional). */
    List<CursoDto> listarActivos(Long idCategoria);

    /** Lista todos, incluidos los inactivos. */
    List<CursoDto> listarTodos();

    /** Obtiene un registro por id (404 si no existe). */
    CursoDto obtener(Long id);

    /** Crea un registro nuevo. */
    CursoDto crear(CursoDto dto);

    /** Actualiza un registro (los campos null no se modifican). */
    CursoDto actualizar(Long id, CursoDto dto);

    /** Activa o desactiva un registro sin borrarlo. */
    CursoDto cambiarActivo(Long id, boolean valor);

    /** Elimina un registro (409 si tiene datos relacionados). */
    void eliminar(Long id);
}
