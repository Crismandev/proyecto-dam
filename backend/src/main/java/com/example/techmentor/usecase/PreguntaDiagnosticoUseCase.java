package com.example.techmentor.usecase;

import java.util.List;
import com.example.techmentor.bean.dto.*;

/** Casos de uso de pregunta diagnostico: operaciones que expone la capa de negocio. */
public interface PreguntaDiagnosticoUseCase {
    /** Lista los registros de un examen. */
    List<PreguntaDiagnosticoDto> listarPorExamen(Long idExamen);
    /** Obtiene un registro por id (404 si no existe). */
    PreguntaDiagnosticoDto obtener(Long id);
    /** Crea un registro nuevo. */
    PreguntaDiagnosticoDto crear(PreguntaDiagnosticoDto dto);
    /** Actualiza un registro existente. */
    PreguntaDiagnosticoDto actualizar(Long id, PreguntaDiagnosticoDto dto);
    /** Elimina un registro (409 si tiene datos relacionados). */
    void eliminar(Long id);
}
