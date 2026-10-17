package com.example.techmentor.usecase;

import java.util.List;
import com.example.techmentor.bean.dto.MisionDto;

/** Casos de uso de misión: operaciones que expone la capa de negocio. */
public interface MisionUseCase {

    /** Lista solo los registros activos. */
    List<MisionDto> listarActivos();

    /** Lista todos, incluidos los inactivos. */
    List<MisionDto> listarTodos();

    /** Obtiene un registro por id (404 si no existe). */
    MisionDto obtener(Long id);

    /** Crea un registro nuevo. */
    MisionDto crear(MisionDto dto);

    /** Actualiza un registro (los campos null no se modifican). */
    MisionDto actualizar(Long id, MisionDto dto);

    /** Activa o desactiva un registro sin borrarlo. */
    MisionDto cambiarActivo(Long id, boolean valor);

    /** Elimina un registro (409 si tiene datos relacionados). */
    void eliminar(Long id);
}
