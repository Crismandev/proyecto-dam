package com.example.techmentor.security;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import com.example.techmentor.usecase.AuthUseCase;

/**
 * No lleva @Component: lo instancia SecurityConfig (así no se registra dos veces).
 */
@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final AuthUseCase authUseCase;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {

        String header = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (header != null && header.startsWith("Bearer ")) {
            try {
                Claims claims = jwtUtil.extraerClaims(header.substring(7));
                Long idUsuario = ((Number) claims.get("idUsuario")).longValue();

                // Antes se conservaban los permisos del token aunque la cuenta cambiara:
                // String rol = claims.get("rol", String.class);
                // UsuarioAutenticado principal = new UsuarioAutenticado(idUsuario, claims.getSubject(), rol);
                UsuarioAutenticado principal = authUseCase.validarSesion(idUsuario, claims.getSubject());
                String rol = principal.rol();
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                        principal, null, List.of(new SimpleGrantedAuthority("ROLE_" + rol)));

                SecurityContextHolder.getContext().setAuthentication(auth);
            } catch (RuntimeException e) {
                SecurityContextHolder.clearContext();
                log.debug("Token inválido: {}", e.getMessage());
            }
        }

        chain.doFilter(request, response);
    }
}
