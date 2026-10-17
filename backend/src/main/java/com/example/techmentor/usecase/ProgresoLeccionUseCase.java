package com.example.techmentor.usecase;

import java.util.List;
import com.example.techmentor.bean.dto.CompletarLeccionDto;
import com.example.techmentor.bean.dto.ProgresoLeccionDto;
import com.example.techmentor.security.UsuarioAutenticado;

/** Casos de uso de progreso leccion: operaciones que expone la capa de negocio. */
public interface ProgresoLeccionUseCase {
    ProgresoLeccionDto iniciar(Long idLeccion, UsuarioAutenticado usuario);
    ProgresoLeccionDto completar(Long idLeccion, CompletarLeccionDto dto, UsuarioAutenticado usuario);
    List<ProgresoLeccionDto> misLecciones(Long idCurso, UsuarioAutenticado usuario);
    ProgresoLeccionDto obtener(Long id, UsuarioAutenticado usuario);
    List<ProgresoLeccionDto> listarPorUsuario(Long idUsuario);
    void eliminar(Long id);
}
