package com.example.techmentor.usecase;

import java.util.List;
import com.example.techmentor.bean.dto.*;
import com.example.techmentor.security.UsuarioAutenticado;

/** Casos de uso de resultado diagnostico: operaciones que expone la capa de negocio. */
public interface ResultadoDiagnosticoUseCase {
    /** Rinde el examen: calcula puntaje y asigna nivel. */
    ResultadoDiagnosticoDto rendir(Long idExamen, RendirExamenDto dto, UsuarioAutenticado usuario);
    /** Resultados del usuario autenticado. */
    List<ResultadoDiagnosticoDto> misResultados(UsuarioAutenticado usuario);
    /** Obtiene un registro por id (404 si no existe). */
    ResultadoDiagnosticoDto obtener(Long id, UsuarioAutenticado usuario);
    /** Lista por usuario (staff). */
    List<ResultadoDiagnosticoDto> listarPorUsuario(Long idUsuario);
    /** Lista los registros de un examen. */
    List<ResultadoDiagnosticoDto> listarPorExamen(Long idExamen);
    /** Cambia manualmente el nivel asignado. */
    ResultadoDiagnosticoDto cambiarNivel(Long id, Long idNivel);
    /** Elimina un registro (409 si tiene datos relacionados). */
    void eliminar(Long id);
}
