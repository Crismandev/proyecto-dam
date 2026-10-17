package com.example.techmentor.usecase;

import java.util.List;
import com.example.techmentor.bean.dto.EjercicioDto;

/** Casos de uso de ejercicio: operaciones que expone la capa de negocio. */
public interface EjercicioUseCase {

    /** Lista todos los registros. */
    List<EjercicioDto> listar();

    /** Lista los registros de un(a) leccion. */
    List<EjercicioDto> listarPorLeccion(Long idLeccion);

    /** Obtiene un registro por id (404 si no existe). */
    EjercicioDto obtener(Long id);

    /** Crea un registro nuevo. */
    EjercicioDto crear(EjercicioDto dto);

    /** Actualiza un registro (los campos null no se modifican). */
    EjercicioDto actualizar(Long id, EjercicioDto dto);

    /** Elimina un registro (409 si tiene datos relacionados). */
    void eliminar(Long id);
}
