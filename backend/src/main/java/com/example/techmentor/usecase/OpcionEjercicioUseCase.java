package com.example.techmentor.usecase;

import java.util.List;
import com.example.techmentor.bean.dto.OpcionEjercicioDto;

/** Casos de uso de opción de ejercicio: operaciones que expone la capa de negocio. */
public interface OpcionEjercicioUseCase {

    /** Lista todos los registros. */
    List<OpcionEjercicioDto> listar();

    /** Lista los registros de un(a) ejercicio. */
    List<OpcionEjercicioDto> listarPorEjercicio(Long idEjercicio);

    /** Obtiene un registro por id (404 si no existe). */
    OpcionEjercicioDto obtener(Long id);

    /** Crea un registro nuevo. */
    OpcionEjercicioDto crear(OpcionEjercicioDto dto);

    /** Actualiza un registro (los campos null no se modifican). */
    OpcionEjercicioDto actualizar(Long id, OpcionEjercicioDto dto);

    /** Elimina un registro (409 si tiene datos relacionados). */
    void eliminar(Long id);
}
