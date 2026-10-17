package com.example.techmentor.usecase;

import java.util.List;
import com.example.techmentor.bean.dto.CategoriaDto;

/** Casos de uso de categoría: operaciones que expone la capa de negocio. */
public interface CategoriaUseCase {

    /** Lista solo los registros activos. */
    List<CategoriaDto> listarActivos();

    /** Lista todos, incluidos los inactivos. */
    List<CategoriaDto> listarTodos();

    /** Obtiene un registro por id (404 si no existe). */
    CategoriaDto obtener(Long id);

    /** Crea un registro nuevo. */
    CategoriaDto crear(CategoriaDto dto);

    /** Actualiza un registro (los campos null no se modifican). */
    CategoriaDto actualizar(Long id, CategoriaDto dto);

    /** Activa o desactiva un registro sin borrarlo. */
    CategoriaDto cambiarActivo(Long id, boolean valor);

    /** Elimina un registro (409 si tiene datos relacionados). */
    void eliminar(Long id);
}
