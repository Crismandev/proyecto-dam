package com.example.techmentor.security;

import com.example.techmentor.usecase.AuthUseCase;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtFilterTest {

    @Mock private JwtUtil jwtUtil;
    @Mock private AuthUseCase authUseCase;
    private JwtFilter filter;

    @BeforeEach
    void preparar() {
        SecurityContextHolder.clearContext();
        filter = new JwtFilter(jwtUtil, authUseCase);
    }

    @AfterEach
    void limpiarSesion() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void usaElRolActualAunqueElTokenTodaviaDigaAdministrador() throws Exception {
        when(jwtUtil.extraerClaims("token")).thenReturn(claims());
        when(authUseCase.validarSesion(7L, "alumno"))
                .thenReturn(new UsuarioAutenticado(7L, "alumno", "ALUMNO"));

        filter.doFilter(peticionConToken(), new MockHttpServletResponse(), new MockFilterChain());

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        assertNotNull(auth);
        assertEquals(new UsuarioAutenticado(7L, "alumno", "ALUMNO"), auth.getPrincipal());
        assertEquals(1, auth.getAuthorities().size());
        assertEquals("ROLE_ALUMNO", auth.getAuthorities().iterator().next().getAuthority());
    }

    @Test
    void noAutenticaUnTokenFirmadoSiLaCuentaFueBloqueadaOEliminada() throws Exception {
        when(jwtUtil.extraerClaims("token")).thenReturn(claims());
        when(authUseCase.validarSesion(7L, "alumno"))
                .thenThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED));
        MockFilterChain chain = new MockFilterChain();

        filter.doFilter(peticionConToken(), new MockHttpServletResponse(), chain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        assertNotNull(chain.getRequest());
    }

    @Test
    void rechazaFirmaInvalidaAntesDeConsultarLaCuenta() throws Exception {
        when(jwtUtil.extraerClaims("token")).thenThrow(new JwtException("Firma invalida"));

        filter.doFilter(peticionConToken(), new MockHttpServletResponse(), new MockFilterChain());

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verifyNoInteractions(authUseCase);
    }

    @Test
    void permiteQueLaCadenaProceseElLoginPublicoSinToken() throws Exception {
        MockFilterChain chain = new MockFilterChain();

        filter.doFilter(new MockHttpServletRequest("POST", "/auth/login"),
                new MockHttpServletResponse(), chain);

        assertNotNull(chain.getRequest());
        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verifyNoInteractions(jwtUtil, authUseCase);
    }

    private MockHttpServletRequest peticionConToken() {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/usuarios/me");
        request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer token");
        return request;
    }

    private Claims claims() {
        Claims claims = Jwts.claims();
        claims.setSubject("alumno");
        claims.put("idUsuario", 7L);
        claims.put("rol", "ADMINISTRADOR");
        return claims;
    }
}
