package com.techmentor.service;

import com.techmentor.config.JwtProvider;
import com.techmentor.dto.LoginRequest;
import com.techmentor.dto.LoginResponse;
import com.techmentor.dto.RegistroRequest;
import com.techmentor.entity.CredencialUsuario;
import com.techmentor.entity.EstadoUsuario;
import com.techmentor.entity.Rol;
import com.techmentor.entity.Usuario;
import com.techmentor.exception.BadRequestException;
import com.techmentor.exception.ConflictException;
import com.techmentor.repository.CredencialUsuarioRepository;
import com.techmentor.repository.EstadoUsuarioRepository;
import com.techmentor.repository.RolRepository;
import com.techmentor.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final UsuarioRepository usuarioRepository;
    private final CredencialUsuarioRepository credencialRepository;
    private final RolRepository rolRepository;
    private final EstadoUsuarioRepository estadoUsuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public LoginResponse login(LoginRequest request) {
        String identifier = request.getUsername();

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(identifier, request.getContrasena())
        );

        CredencialUsuario credencial = credencialRepository.findByUsername(identifier)
                .or(() -> credencialRepository.findByUsuarioEmail(identifier))
                .orElseThrow(() -> new BadRequestException("Usuario no encontrado"));

        Usuario usuario = credencial.getUsuario();
        String token = jwtProvider.generarToken(authentication);

        return LoginResponse.builder()
                .token(token)
                .rol(usuario.getRol().getNombre())
                .idUsuario(usuario.getIdUsuario())
                .nombres(usuario.getNombres())
                .build();
    }

    @Transactional
    public LoginResponse registro(RegistroRequest request) {
        if (credencialRepository.existsByUsername(request.getUsername())) {
            throw new ConflictException("El username ya está en uso");
        }
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new ConflictException("El correo ya se encuentra registrado");
        }

        Rol rolAlumno = rolRepository.findByNombre("ALUMNO")
                .orElseThrow(() -> new BadRequestException("El rol ALUMNO no existe en el sistema"));

        EstadoUsuario estadoActivo = estadoUsuarioRepository.findByNombre("ACTIVO")
                .orElseThrow(() -> new BadRequestException("El estado ACTIVO no existe en el sistema"));

        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setNombres(request.getNombres());
        nuevoUsuario.setApellidos(request.getApellidos() != null ? request.getApellidos() : "");
        nuevoUsuario.setEmail(request.getEmail());
        nuevoUsuario.setRol(rolAlumno);
        nuevoUsuario.setEstadoUsuario(estadoActivo);
        nuevoUsuario.setXpTotal(0);
        nuevoUsuario.setMonedas(0);
        nuevoUsuario.setNivelUsuario(1);

        Usuario guardado = usuarioRepository.save(nuevoUsuario);

        CredencialUsuario credencial = new CredencialUsuario();
        credencial.setUsuario(guardado);
        credencial.setUsername(request.getUsername());
        credencial.setPasswordHash(passwordEncoder.encode(request.getContrasena()));
        credencialRepository.save(credencial);

        String token = jwtProvider.generarTokenPorUsername(request.getUsername());

        return LoginResponse.builder()
                .token(token)
                .rol(rolAlumno.getNombre())
                .idUsuario(guardado.getIdUsuario())
                .nombres(guardado.getNombres())
                .build();
    }
}
