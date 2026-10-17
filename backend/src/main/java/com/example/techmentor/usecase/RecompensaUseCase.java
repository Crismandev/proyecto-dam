package com.example.techmentor.usecase;

import java.util.List;
import com.example.techmentor.bean.dto.RecompensaDto;

/** Casos de uso de recompensa: operaciones que expone la capa de negocio. */
public interface RecompensaUseCase {

    /** Lista solo los registros activos. */
    List<RecompensaDto> listarActivos();

    /** Lista todos, incluidos los inactivos. */
    List<RecompensaDto> listarTodos();

    /** Obtiene un registro por id (404 si no existe). */
    RecompensaDto obtener(Long id);

    /** Crea un registro nuevo. */
    RecompensaDto crear(RecompensaDto dto);

    /** Actualiza un registro (los campos null no se modifican). */
    RecompensaDto actualizar(Long id, RecompensaDto dto);

    /** Activa o desactiva un registro sin borrarlo. */
    RecompensaDto cambiarActivo(Long id, boolean valor);

    /** Elimina un registro (409 si tiene datos relacionados). */
    void eliminar(Long id);
}
