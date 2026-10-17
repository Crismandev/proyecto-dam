package com.example.techmentor.usecase;

import java.util.List;
import com.example.techmentor.bean.dto.LeccionDto;

/** Casos de uso de lección: operaciones que expone la capa de negocio. */
public interface LeccionUseCase {

    /** Lista todos los registros. */
    List<LeccionDto> listar();

    /** Lista los registros de un(a) nivel. */
    List<LeccionDto> listarPorNivel(Long idNivel);

    /** Obtiene un registro por id (404 si no existe). */
    LeccionDto obtener(Long id);

    /** Crea un registro nuevo. */
    LeccionDto crear(LeccionDto dto);

    /** Actualiza un registro (los campos null no se modifican). */
    LeccionDto actualizar(Long id, LeccionDto dto);

    /** Elimina un registro (409 si tiene datos relacionados). */
    void eliminar(Long id);
}
