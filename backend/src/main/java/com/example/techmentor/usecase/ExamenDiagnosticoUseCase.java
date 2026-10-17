package com.example.techmentor.usecase;

import java.util.List;
import com.example.techmentor.bean.dto.ExamenDiagnosticoDto;
import com.example.techmentor.bean.dto.ExamenParaRendirDto;

/** Casos de uso de exámenes diagnóstico (el examen se adapta al puesto/profesión del usuario). */
public interface ExamenDiagnosticoUseCase {

    /** Lista exámenes activos; filtros opcionales por curso y por puesto (profesión). */
    List<ExamenDiagnosticoDto> listarActivos(Long idCurso, Long idProfesion);

    /** Exámenes activos que corresponden al puesto principal del usuario (más los generales). */
    List<ExamenDiagnosticoDto> listarParaUsuario(Long idUsuario);

    /** Lista todos, incluidos los inactivos (staff). */
    List<ExamenDiagnosticoDto> listarTodos();

    /** Obtiene un examen por id. */
    ExamenDiagnosticoDto obtener(Long id);

    /** Examen con preguntas y opciones SIN la respuesta correcta, listo para rendir. */
    ExamenParaRendirDto obtenerParaRendir(Long id);

    /** Crea un examen. */
    ExamenDiagnosticoDto crear(ExamenDiagnosticoDto dto);

    /** Actualiza un examen (también sirve para activarlo o desactivarlo con "activo"). */
    ExamenDiagnosticoDto actualizar(Long id, ExamenDiagnosticoDto dto);

    /** Elimina un examen (409 si ya tiene resultados; en ese caso desactívalo). */
    void eliminar(Long id);
}
