package com.example.techmentor.usecase;

import java.util.List;
import com.example.techmentor.bean.dto.EstadoUsuarioDto;

/** Casos de uso de estado de usuario: operaciones que expone la capa de negocio. */
public interface EstadoUsuarioUseCase {

    /** Lista todos los registros. */
    List<EstadoUsuarioDto> listar();

    /** Obtiene un registro por id (404 si no existe). */
    EstadoUsuarioDto obtener(Long id);

    /** Crea un registro nuevo. */
    EstadoUsuarioDto crear(EstadoUsuarioDto dto);

    /** Actualiza un registro (los campos null no se modifican). */
    EstadoUsuarioDto actualizar(Long id, EstadoUsuarioDto dto);

    /** Elimina un registro (409 si tiene datos relacionados). */
    void eliminar(Long id);
}
