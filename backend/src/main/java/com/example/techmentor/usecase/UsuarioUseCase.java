package com.example.techmentor.usecase;

import java.util.List;
import com.example.techmentor.bean.dto.*;
import com.example.techmentor.security.UsuarioAutenticado;

/** Casos de uso de usuarios: consulta, alta por ADMINISTRADOR, perfil, estado, rol y baja. */
public interface UsuarioUseCase {

    /** Lista todos los usuarios (staff). */
    List<UsuarioDto> listar();

    /** Obtiene un usuario: solo él mismo o ADMINISTRADOR/MAESTRO. */
    UsuarioDto obtener(Long id, UsuarioAutenticado solicitante);

    /** Perfil del usuario autenticado. */
    UsuarioDto miPerfil(UsuarioAutenticado usuario);

    /** Crea un usuario con su credencial (ADMINISTRADOR). El correo queda verificado. */
    UsuarioDto crear(UsuarioCrearDto dto);

    /** Edita el perfil de cualquier usuario (ADMINISTRADOR). */
    UsuarioDto actualizarPerfil(Long id, UsuarioPerfilDto dto);

    /** Edita el perfil propio. */
    UsuarioDto actualizarMiPerfil(UsuarioAutenticado usuario, UsuarioPerfilDto dto);

    /** Cambia el estado (activar, bloquear, suspender...) y lo registra en el historial. */
    UsuarioDto cambiarEstado(Long id, CambiarEstadoDto dto, UsuarioAutenticado admin);

    /** Cambia el rol de un usuario. */
    UsuarioDto cambiarRol(Long id, CambiarRolDto dto, UsuarioAutenticado admin);

    /** Elimina un usuario y sus datos en cascada (prefiere cambiar el estado). */
    void eliminar(Long id, UsuarioAutenticado admin);

    /** Historial de cambios de estado de un usuario. */
    List<HistorialEstadoDto> historial(Long idUsuario);
}
