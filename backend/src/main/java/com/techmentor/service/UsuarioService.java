package com.techmentor.service;

import com.techmentor.dto.AlumnoDetalleDTO;
import com.techmentor.dto.UsuarioDTO;
import com.techmentor.dto.UsuarioSaveDTO;
import com.techmentor.entity.CredencialUsuario;
import com.techmentor.entity.Curso;
import com.techmentor.entity.EstadoUsuario;
import com.techmentor.entity.ProgresoLeccion;
import com.techmentor.entity.Rol;
import com.techmentor.entity.Usuario;
import com.techmentor.exception.BadRequestException;
import com.techmentor.exception.ConflictException;
import com.techmentor.exception.ResourceNotFoundException;
import com.techmentor.mapper.UsuarioMapper;
import com.techmentor.repository.CredencialUsuarioRepository;
import com.techmentor.repository.EstadoUsuarioRepository;
import com.techmentor.repository.ProgresoLeccionRepository;
import com.techmentor.repository.RolRepository;
import com.techmentor.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final EstadoUsuarioRepository estadoUsuarioRepository;
    private final CredencialUsuarioRepository credencialUsuarioRepository;
    private final ProgresoLeccionRepository progresoLeccionRepository;
    private final UsuarioMapper usuarioMapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public Page<UsuarioDTO> listar(String q, Pageable pageable) {
        String query = q != null ? q.trim() : "";
        return usuarioRepository.buscar(query, pageable).map(usuarioMapper::toDTO);
    }

    @Transactional(readOnly = true)
    public UsuarioDTO obtenerPorId(Long id) {
        Usuario entity = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + id));
        return usuarioMapper.toDTO(entity);
    }

    @Transactional
    public UsuarioDTO crear(UsuarioSaveDTO dto) {
        if (usuarioRepository.existsByEmail(dto.getCorreo())) {
            throw new ConflictException("El correo ya se encuentra registrado");
        }
        if (!StringUtils.hasText(dto.getContrasena()) || dto.getContrasena().length() < 8) {
            throw new BadRequestException("La contraseña es obligatoria y debe tener al menos 8 caracteres");
        }

        Rol rol = rolRepository.findById(dto.getIdRol())
                .orElseThrow(() -> new ResourceNotFoundException("Rol no encontrado con ID: " + dto.getIdRol()));

        EstadoUsuario estadoActivo = estadoUsuarioRepository.findByNombre("ACTIVO")
                .orElseGet(() -> estadoUsuarioRepository.findAll().stream().findFirst()
                        .orElseThrow(() -> new ResourceNotFoundException("Estado de usuario no disponible")));

        Usuario entity = new Usuario();
        entity.setRol(rol);
        entity.setEstadoUsuario(estadoActivo);
        entity.setNombres(dto.getNombres());
        entity.setApellidos(dto.getApellidos());
        entity.setEmail(dto.getCorreo());
        entity.setFechaNacimiento(dto.getFechaNacimiento());
        entity.setAvatar(dto.getAvatar());
        entity.setXpTotal(0);
        entity.setMonedas(0);

        Usuario guardado = usuarioRepository.save(entity);

        // Guardar credenciales de acceso (username = correo por defecto para usuarios creados en panel)
        CredencialUsuario cred = new CredencialUsuario();
        cred.setUsuario(guardado);
        cred.setUsername(dto.getCorreo());
        cred.setPasswordHash(passwordEncoder.encode(dto.getContrasena()));
        credencialUsuarioRepository.save(cred);

        return usuarioMapper.toDTO(guardado);
    }

    @Transactional
    public UsuarioDTO actualizar(Long id, UsuarioSaveDTO dto) {
        Usuario entity = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + id));

        if (!entity.getEmail().equalsIgnoreCase(dto.getCorreo()) && usuarioRepository.existsByEmail(dto.getCorreo())) {
            throw new ConflictException("El correo ya se encuentra registrado por otro usuario");
        }

        Rol rol = rolRepository.findById(dto.getIdRol())
                .orElseThrow(() -> new ResourceNotFoundException("Rol no encontrado con ID: " + dto.getIdRol()));

        entity.setRol(rol);
        entity.setNombres(dto.getNombres());
        entity.setApellidos(dto.getApellidos());
        entity.setEmail(dto.getCorreo());
        entity.setFechaNacimiento(dto.getFechaNacimiento());
        entity.setAvatar(dto.getAvatar());

        if (StringUtils.hasText(dto.getContrasena())) {
            if (dto.getContrasena().length() < 8) {
                throw new BadRequestException("La contraseña debe tener al menos 8 caracteres");
            }
            CredencialUsuario cred = credencialUsuarioRepository.findByUsuarioIdUsuario(id)
                    .orElseGet(() -> {
                        CredencialUsuario c = new CredencialUsuario();
                        c.setUsuario(entity);
                        c.setUsername(dto.getCorreo());
                        return c;
                    });
            cred.setPasswordHash(passwordEncoder.encode(dto.getContrasena()));
            credencialUsuarioRepository.save(cred);
        }

        Usuario actualizado = usuarioRepository.save(entity);
        return usuarioMapper.toDTO(actualizado);
    }

    @Transactional
    public void eliminar(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new ResourceNotFoundException("Usuario no encontrado con ID: " + id);
        }
        usuarioRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public AlumnoDetalleDTO obtenerDetalleAlumno(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Alumno no encontrado con ID: " + id));

        List<ProgresoLeccion> progresos = progresoLeccionRepository.findByUsuarioIdUsuario(id);

        List<AlumnoDetalleDTO.ProgresoAlumnoDTO> progresoDTOs = progresos.stream()
                .map(p -> AlumnoDetalleDTO.ProgresoAlumnoDTO.builder()
                        .idProgreso(p.getIdProgreso())
                        .curso(p.getLeccion() != null && p.getLeccion().getNivel() != null && p.getLeccion().getNivel().getCurso() != null ? p.getLeccion().getNivel().getCurso().getNombre() : "")
                        .leccion(p.getLeccion() != null ? p.getLeccion().getTitulo() : "")
                        .aciertos(p.getPuntaje())
                        .totalPreguntas(10)
                        .xpGanado(p.getXpGanado())
                        .fecha(p.getFechaCompletado() != null ? p.getFechaCompletado().toLocalDateTime() : null)
                        .build())
                .collect(Collectors.toList());

        Map<Long, AlumnoDetalleDTO.CursoAlumnoDTO> cursoMap = new LinkedHashMap<>();
        for (ProgresoLeccion p : progresos) {
            if (p.getLeccion() != null && p.getLeccion().getNivel() != null && p.getLeccion().getNivel().getCurso() != null) {
                Curso c = p.getLeccion().getNivel().getCurso();
                cursoMap.putIfAbsent(c.getIdCurso(), AlumnoDetalleDTO.CursoAlumnoDTO.builder()
                        .idCurso(c.getIdCurso())
                        .nombreCurso(c.getNombre())
                        .descripcion(c.getDescripcion())
                        .icono(c.getIcono())
                        .dificultad("INTERMEDIO")
                        .leccionesCompletadas(0)
                        .build());

                AlumnoDetalleDTO.CursoAlumnoDTO cursoDTO = cursoMap.get(c.getIdCurso());
                cursoDTO.setLeccionesCompletadas(cursoDTO.getLeccionesCompletadas() + 1);
            }
        }

        LocalDateTime fechaReg = usuario.getFechaRegistro() != null ? usuario.getFechaRegistro().toLocalDateTime() : LocalDateTime.now();

        return AlumnoDetalleDTO.builder()
                .idUsuario(usuario.getIdUsuario())
                .nombres(usuario.getNombres())
                .apellidos(usuario.getApellidos())
                .correo(usuario.getEmail())
                .rol(usuario.getRol() != null ? usuario.getRol().getNombre() : "ESTUDIANTE")
                .xpTotal(usuario.getXpTotal())
                .monedas(usuario.getMonedas())
                .fechaRegistro(fechaReg)
                .fechaNacimiento(usuario.getFechaNacimiento())
                .avatar(usuario.getAvatar())
                .progreso(progresoDTOs)
                .cursos(new ArrayList<>(cursoMap.values()))
                .build();
    }
}
