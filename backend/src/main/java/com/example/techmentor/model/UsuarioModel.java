package com.example.techmentor.model;

import java.time.OffsetDateTime;
import java.util.List;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import com.example.techmentor.bean.dto.*;
import com.example.techmentor.bean.entity.*;
import com.example.techmentor.bean.mapper.HistorialEstadoMapper;
import com.example.techmentor.bean.mapper.UsuarioMapper;
import com.example.techmentor.persistence.*;
import com.example.techmentor.security.UsuarioAutenticado;
import com.example.techmentor.usecase.UsuarioUseCase;

/** Lógica de usuarios: alta con credencial, perfil, cambio de estado/rol (con historial) y baja. */
@Service
@RequiredArgsConstructor
public class UsuarioModel implements UsuarioUseCase {

    private static final String ESTADO_ACTIVO = "ACTIVO";
    private static final String ROL_ADMIN = "ADMINISTRADOR";

    private final UsuarioRepository usuarioRepository;
    private final CredencialUsuarioRepository credencialRepository;
    private final RolRepository rolRepository;
    private final EstadoUsuarioRepository estadoUsuarioRepository;
    private final HistorialEstadoUsuarioRepository historialRepository;
    private final PasswordEncoder passwordEncoder;
    private final UsuarioMapper usuarioMapper;
    private final HistorialEstadoMapper historialMapper;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioDto> listar() {
        return usuarioRepository.findAll().stream().map(usuarioMapper::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public UsuarioDto obtener(Long id, UsuarioAutenticado solicitante) {
        solicitante.exigirPropietarioOStaff(id);
        return usuarioMapper.toDto(buscar(id));
    }

    @Override
    @Transactional(readOnly = true)
    public UsuarioDto miPerfil(UsuarioAutenticado usuario) {
        return usuarioMapper.toDto(buscar(usuario.idUsuario()));
    }

    @Override
    @Transactional
    public UsuarioDto crear(UsuarioCrearDto dto) {
        if (usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El correo ya está registrado");
        }
        if (credencialRepository.existsByUsername(dto.getUsername())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El username ya existe");
        }
        Rol rol = buscarRol(dto.getIdRol());
        EstadoUsuario estado = (dto.getIdEstadoUsuario() != null)
                ? buscarEstado(dto.getIdEstadoUsuario())
                : estadoUsuarioRepository.findByNombre(ESTADO_ACTIVO).orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.INTERNAL_SERVER_ERROR, "Falta el estado ACTIVO en la base de datos"));

        Usuario usuario = usuarioRepository.saveAndFlush(Usuario.builder()
                .rol(rol)
                .estadoUsuario(estado)
                .nombres(dto.getNombres())
                .apellidos(dto.getApellidos())
                .email(dto.getEmail())
                .fechaNacimiento(dto.getFechaNacimiento())
                .telefono(dto.getTelefono())
                .pais(dto.getPais())
                .ciudad(dto.getCiudad())
                .emailVerificado(true)                       // alta hecha por un administrador
                .fechaVerificacionEmail(OffsetDateTime.now())
                .build());

        credencialRepository.save(CredencialUsuario.builder()
                .usuario(usuario)
                .username(dto.getUsername())
                .passwordHash(passwordEncoder.encode(dto.getPassword()))
                .build());

        historialRepository.save(HistorialEstadoUsuario.builder()
                .usuario(usuario).estadoUsuario(estado).motivo("Alta de usuario").build());

        entityManager.flush();
        entityManager.refresh(usuario); // trae fecha_registro (la genera la BD)
        return usuarioMapper.toDto(usuario);
    }

    @Override
    @Transactional
    public UsuarioDto actualizarPerfil(Long id, UsuarioPerfilDto dto) {
        Usuario usuario = buscar(id);
        usuarioMapper.actualizarPerfil(dto, usuario);
        return usuarioMapper.toDto(usuarioRepository.saveAndFlush(usuario));
    }

    @Override
    @Transactional
    public UsuarioDto actualizarMiPerfil(UsuarioAutenticado usuario, UsuarioPerfilDto dto) {
        return actualizarPerfil(usuario.idUsuario(), dto);
    }

    @Override
    @Transactional
    public UsuarioDto cambiarEstado(Long id, CambiarEstadoDto dto, UsuarioAutenticado admin) {
        Usuario usuario = buscar(id);
        EstadoUsuario nuevo = buscarEstado(dto.getIdEstadoUsuario());
        if (usuario.getIdUsuario().equals(admin.idUsuario()) && !ESTADO_ACTIVO.equals(nuevo.getNombre())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No puedes desactivar tu propia cuenta");
        }
        usuario.setEstadoUsuario(nuevo);
        historialRepository.save(HistorialEstadoUsuario.builder()
                .usuario(usuario).estadoUsuario(nuevo).motivo(dto.getMotivo()).build());
        return usuarioMapper.toDto(usuarioRepository.saveAndFlush(usuario));
    }

    @Override
    @Transactional
    public UsuarioDto cambiarRol(Long id, CambiarRolDto dto, UsuarioAutenticado admin) {
        Usuario usuario = buscar(id);
        Rol nuevo = buscarRol(dto.getIdRol());
        if (usuario.getIdUsuario().equals(admin.idUsuario()) && !ROL_ADMIN.equals(nuevo.getNombre())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No puedes quitarte tu propio rol de administrador");
        }
        usuario.setRol(nuevo);
        return usuarioMapper.toDto(usuarioRepository.saveAndFlush(usuario));
    }

    @Override
    @Transactional
    public void eliminar(Long id, UsuarioAutenticado admin) {
        if (id.equals(admin.idUsuario())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No puedes eliminar tu propia cuenta");
        }
        usuarioRepository.delete(buscar(id));
        usuarioRepository.flush();
    }

    @Override
    @Transactional(readOnly = true)
    public List<HistorialEstadoDto> historial(Long idUsuario) {
        buscar(idUsuario);
        return historialRepository.findByUsuario_IdUsuarioOrderByFechaCambioDesc(idUsuario)
                .stream().map(historialMapper::toDto).toList();
    }

    private Usuario buscar(Long id) {
        return usuarioRepository.findById(id).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Usuario no encontrado: " + id));
    }

    private Rol buscarRol(Long id) {
        return rolRepository.findById(id).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Rol no encontrado: " + id));
    }

    private EstadoUsuario buscarEstado(Long id) {
        return estadoUsuarioRepository.findById(id).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Estado no encontrado: " + id));
    }
}
