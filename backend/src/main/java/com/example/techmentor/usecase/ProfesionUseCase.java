package com.example.techmentor.usecase;

import java.util.List;
import com.example.techmentor.bean.dto.ProfesionDto;

/** Casos de uso de profesión: operaciones que expone la capa de negocio. */
public interface ProfesionUseCase {

    /** Lista solo los registros activos. */
    List<ProfesionDto> listarActivos();

    /** Lista todos, incluidos los inactivos. */
    List<ProfesionDto> listarTodos();

    /** Obtiene un registro por id (404 si no existe). */
    ProfesionDto obtener(Long id);

    /** Crea un registro nuevo. */
    ProfesionDto crear(ProfesionDto dto);

    /** Actualiza un registro (los campos null no se modifican). */
    ProfesionDto actualizar(Long id, ProfesionDto dto);

    /** Activa o desactiva un registro sin borrarlo. */
    ProfesionDto cambiarActivo(Long id, boolean valor);

    /** Elimina un registro (409 si tiene datos relacionados). */
    void eliminar(Long id);
}
