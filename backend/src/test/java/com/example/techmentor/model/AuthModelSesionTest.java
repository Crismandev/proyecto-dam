package com.example.techmentor.model;

import com.example.techmentor.bean.entity.CredencialUsuario;
import com.example.techmentor.bean.entity.EstadoUsuario;
import com.example.techmentor.bean.entity.Rol;
import com.example.techmentor.bean.entity.Usuario;
import com.example.techmentor.persistence.CredencialUsuarioRepository;
import com.example.techmentor.security.JwtUtil;
import com.example.techmentor.security.UsuarioAutenticado;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;
import lombok.extern.slf4j.Slf4j;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthModelSesionTest {

    @Mock private CredencialUsuarioRepository credencialRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private JwtUtil jwtUtil;

    private AuthModel model;

    @BeforeEach
    void preparar() {
        model = new AuthModel(credencialRepository, passwordEncoder, jwtUtil);
    }

    @Test
    void conservaIdentidadDeUnaCuentaActiva() {
        when(credencialRepository.findByUsername("alumno"))
                .thenReturn(Optional.of(credencial("ACTIVO", "ALUMNO")));

        UsuarioAutenticado sesion = model.validarSesion(7L, "alumno");

        assertEquals(new UsuarioAutenticado(7L, "alumno", "ALUMNO"), sesion);
    }

    @ParameterizedTest
    @ValueSource(strings = {"BLOQUEADO", "INACTIVO", "ELIMINADO"})
    void rechazaLaSesionCuandoLaCuentaDejaDeEstarActiva(String estado) {
        when(credencialRepository.findByUsername("alumno"))
                .thenReturn(Optional.of(credencial(estado, "ALUMNO")));

        ResponseStatusException error = assertThrows(ResponseStatusException.class,
                () -> model.validarSesion(7L, "alumno"));

        assertEquals(HttpStatus.UNAUTHORIZED, error.getStatusCode());
    }

    @Test
    void actualizaPermisosSiElAdministradorCambiaElRolDuranteLaSesion() {
        CredencialUsuario credencial = credencial("ACTIVO", "ADMINISTRADOR");
        when(credencialRepository.findByUsername("alumno")).thenReturn(Optional.of(credencial));
        assertEquals("ADMINISTRADOR", model.validarSesion(7L, "alumno").rol());

        credencial.getUsuario().setRol(Rol.builder().nombre("ALUMNO").build());

        assertEquals("ALUMNO", model.validarSesion(7L, "alumno").rol());
    }

    @Test
    void rechazaLaSesionSiLaCuentaFueBorrada() {
        when(credencialRepository.findByUsername("alumno")).thenReturn(Optional.empty());

        ResponseStatusException error = assertThrows(ResponseStatusException.class,
                () -> model.validarSesion(7L, "alumno"));

        assertEquals(HttpStatus.UNAUTHORIZED, error.getStatusCode());
    }

    @Test
    void noPermiteUsarUnTokenAnteriorSiElUsernameAhoraPerteneceAOtroUsuario() {
        CredencialUsuario credencial = credencial("ACTIVO", "ADMINISTRADOR");
        credencial.getUsuario().setIdUsuario(8L);
        when(credencialRepository.findByUsername("alumno")).thenReturn(Optional.of(credencial));

        assertThrows(ResponseStatusException.class, () -> model.validarSesion(7L, "alumno"));
    }

    @Test
    void rechazaTokensSinIdentidadSinConsultarLaBase() {
        assertThrows(ResponseStatusException.class, () -> model.validarSesion(null, "alumno"));
        assertThrows(ResponseStatusException.class, () -> model.validarSesion(7L, " "));
        verifyNoInteractions(credencialRepository);
    }

    private CredencialUsuario credencial(String estado, String rol) {
        Usuario usuario = Usuario.builder()
                .idUsuario(7L)
                .rol(Rol.builder().nombre(rol).build())
                .estadoUsuario(EstadoUsuario.builder().nombre(estado).build())
                .build();
        return CredencialUsuario.builder().usuario(usuario).username("alumno").build();
    }
}
