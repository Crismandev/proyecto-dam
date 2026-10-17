package com.example.techmentor.usecase;

import java.util.List;
import com.example.techmentor.bean.dto.*;

/** Casos de uso de opcion diagnostico: operaciones que expone la capa de negocio. */
public interface OpcionDiagnosticoUseCase {
    /** Lista las opciones de una pregunta. */
    List<OpcionDiagnosticoDto> listarPorPregunta(Long idPregunta);
    /** Obtiene un registro por id (404 si no existe). */
    OpcionDiagnosticoDto obtener(Long id);
    /** Crea un registro nuevo. */
    OpcionDiagnosticoDto crear(OpcionDiagnosticoDto dto);
    /** Actualiza un registro existente. */
    OpcionDiagnosticoDto actualizar(Long id, OpcionDiagnosticoDto dto);
    /** Elimina un registro (409 si tiene datos relacionados). */
    void eliminar(Long id);
}
