package com.example.techmentor.usecase;

import java.util.List;
import com.example.techmentor.bean.dto.RolDto;

/** Casos de uso de rol: operaciones que expone la capa de negocio. */
public interface RolUseCase {

    /** Lista todos los registros. */
    List<RolDto> listar();

    /** Obtiene un registro por id (404 si no existe). */
    RolDto obtener(Long id);

    /** Crea un registro nuevo. */
    RolDto crear(RolDto dto);

    /** Actualiza un registro (los campos null no se modifican). */
    RolDto actualizar(Long id, RolDto dto);

    /** Elimina un registro (409 si tiene datos relacionados). */
    void eliminar(Long id);
}
