package com.example.techmentor.model;

import java.time.OffsetDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import com.example.techmentor.bean.dto.CambiarClaveDto;
import com.example.techmentor.bean.dto.LoginDto;
import com.example.techmentor.bean.dto.TokenDto;
import com.example.techmentor.bean.entity.CredencialUsuario;
import com.example.techmentor.bean.entity.Usuario;
import com.example.techmentor.persistence.CredencialUsuarioRepository;
import com.example.techmentor.security.JwtUtil;
import com.example.techmentor.security.UsuarioAutenticado;
import com.example.techmentor.usecase.AuthUseCase;

/** Lógica de autenticación: valida credenciales, genera el JWT y cambia la contraseña. */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthModel implements AuthUseCase {

    private static final String ESTADO_ACTIVO = "ACTIVO";

    private final CredencialUsuarioRepository credencialRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    @Transactional
    public TokenDto login(LoginDto dto) {
        CredencialUsuario credencial = credencialRepository.findByUsername(dto.getUsername())
                .orElseThrow(() -> {
                    return credencialesInvalidas();
                });

        boolean ok = passwordEncoder.matches(dto.getPassword(), credencial.getPasswordHash());

        Usuario usuario = credencial.getUsuario();
        String estado = usuario.getEstadoUsuario().getNombre();

        credencial.setUltimoLogin(OffsetDateTime.now());

        String rol = usuario.getRol().getNombre();

        return TokenDto.builder()
                .token(jwtUtil.generarToken(usuario.getIdUsuario(), credencial.getUsername(), rol))
                .tipo("Bearer")
                .idUsuario(usuario.getIdUsuario())
                .username(credencial.getUsername())
                .rol(rol)
                .build();
    }

    @Override
    @Transactional
    public void cambiarClave(UsuarioAutenticado usuario, CambiarClaveDto dto) {
        CredencialUsuario credencial = credencialRepository.findByUsuario_IdUsuario(usuario.idUsuario())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Esta cuenta no tiene contraseña (usa tu login de Google/GitHub)"));
        if (!passwordEncoder.matches(dto.getClaveActual(), credencial.getPasswordHash())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La clave actual no es correcta");
        }
        credencial.setPasswordHash(passwordEncoder.encode(dto.getClaveNueva()));
    }

    @Override
    @Transactional(readOnly = true)
    public UsuarioAutenticado validarSesion(Long idUsuario, String username) {
        if (idUsuario == null || username == null || username.isBlank()) {
            throw credencialesInvalidas();
        }

        CredencialUsuario credencial = credencialRepository.findByUsername(username)
                .orElseThrow(this::credencialesInvalidas);
        Usuario usuario = credencial.getUsuario();
        if (usuario == null || !idUsuario.equals(usuario.getIdUsuario())
                || usuario.getEstadoUsuario() == null
                || !ESTADO_ACTIVO.equals(usuario.getEstadoUsuario().getNombre())
                || usuario.getRol() == null || usuario.getRol().getNombre() == null
                || usuario.getRol().getNombre().isBlank()) {
            throw credencialesInvalidas();
        }

        // El JWT identifica la cuenta; su estado y rol pueden cambiar antes de que expire.
        return new UsuarioAutenticado(usuario.getIdUsuario(), credencial.getUsername(),
                usuario.getRol().getNombre());
    }

    private ResponseStatusException credencialesInvalidas() {
        return new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales inválidas");
    }
}