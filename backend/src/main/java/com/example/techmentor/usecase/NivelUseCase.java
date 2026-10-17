package com.example.techmentor.usecase;

import java.util.List;
import com.example.techmentor.bean.dto.NivelDto;

/** Casos de uso de nivel: operaciones que expone la capa de negocio. */
public interface NivelUseCase {

    /** Lista todos los registros. */
    List<NivelDto> listar();

    /** Lista los registros de un(a) curso. */
    List<NivelDto> listarPorCurso(Long idCurso);

    /** Obtiene un registro por id (404 si no existe). */
    NivelDto obtener(Long id);

    /** Crea un registro nuevo. */
    NivelDto crear(NivelDto dto);

    /** Actualiza un registro (los campos null no se modifican). */
    NivelDto actualizar(Long id, NivelDto dto);

    /** Elimina un registro (409 si tiene datos relacionados). */
    void eliminar(Long id);
}
