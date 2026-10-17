package com.example.techmentor.usecase;

import java.util.List;
import com.example.techmentor.bean.dto.*;
import com.example.techmentor.security.UsuarioAutenticado;

/** Casos de uso de respuesta diagnostico: operaciones que expone la capa de negocio. */
public interface RespuestaDiagnosticoUseCase {
    /** Lista las respuestas de un resultado (dueño o staff). */
    List<RespuestaDiagnosticoDto> listarPorResultado(Long idResultado, UsuarioAutenticado usuario);
    /** Obtiene un registro por id (404 si no existe). */
    RespuestaDiagnosticoDto obtener(Long id, UsuarioAutenticado usuario);
}
