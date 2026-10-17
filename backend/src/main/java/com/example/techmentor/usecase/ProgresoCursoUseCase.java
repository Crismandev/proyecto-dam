package com.example.techmentor.usecase;

import java.util.List;
import com.example.techmentor.bean.dto.ProgresoCursoDto;
import com.example.techmentor.security.UsuarioAutenticado;

/** Casos de uso de progreso curso: operaciones que expone la capa de negocio. */
public interface ProgresoCursoUseCase {
    ProgresoCursoDto iniciar(Long idCurso, UsuarioAutenticado usuario);
    ProgresoCursoDto abandonar(Long idCurso, UsuarioAutenticado usuario);
    List<ProgresoCursoDto> misCursos(UsuarioAutenticado usuario);
    ProgresoCursoDto miProgreso(Long idCurso, UsuarioAutenticado usuario);
    ProgresoCursoDto obtener(Long id, UsuarioAutenticado usuario);
    List<ProgresoCursoDto> listarPorUsuario(Long idUsuario);
    List<ProgresoCursoDto> listarPorCurso(Long idCurso);
    void eliminar(Long id);
}
