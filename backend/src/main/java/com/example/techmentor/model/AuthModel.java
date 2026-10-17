package com.example.techmentor.model;

import java.time.OffsetDateTime;
import lombok.RequiredArgsConstructor;
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
                .orElseThrow(this::credencialesInvalidas);

        if (!passwordEncoder.matches(dto.getPassword(), credencial.getPasswordHash())) {
            throw credencialesInvalidas();
        }

        Usuario usuario = credencial.getUsuario();
        String estado = usuario.getEstadoUsuario().getNombre();
        if (!ESTADO_ACTIVO.equals(estado)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Usuario no activo (" + estado + ")");
        }

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

    private ResponseStatusException credencialesInvalidas() {
        return new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales inválidas");
    }
}
