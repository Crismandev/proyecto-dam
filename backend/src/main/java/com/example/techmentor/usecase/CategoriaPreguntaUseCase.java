package com.example.techmentor.usecase;

import java.util.List;
import com.example.techmentor.bean.dto.*;

/** Casos de uso de categoria pregunta: operaciones que expone la capa de negocio. */
public interface CategoriaPreguntaUseCase {
    /** Lista todos los registros. */
    List<CategoriaPreguntaDto> listar();
    /** Obtiene un registro por id (404 si no existe). */
    CategoriaPreguntaDto obtener(Long id);
    /** Crea un registro nuevo. */
    CategoriaPreguntaDto crear(CategoriaPreguntaDto dto);
    /** Actualiza un registro existente. */
    CategoriaPreguntaDto actualizar(Long id, CategoriaPreguntaDto dto);
    /** Elimina un registro (409 si tiene datos relacionados). */
    void eliminar(Long id);
}
