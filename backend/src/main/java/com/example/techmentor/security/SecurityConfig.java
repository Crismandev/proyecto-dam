package com.example.techmentor.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.example.techmentor.usecase.AuthUseCase;

import org.springframework.http.HttpMethod;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.List;

/** Configuración de Spring Security: API sin sesión (JWT), /auth/login público y permisos por método (@PreAuthorize). */
@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtUtil jwtUtil;

    @Bean
    // Antes: SecurityFilterChain filterChain(HttpSecurity http) throws Exception
    SecurityFilterChain filterChain(HttpSecurity http, AuthUseCase authUseCase) throws Exception {
        // --- Versión anterior sin CORS (comentada): ---
        // http.csrf(csrf -> csrf.disable())
        //         .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        //         .authorizeHttpRequests(auth -> auth
        //                 .requestMatchers("/auth/login", "/error").permitAll()
        //                 .anyRequest().authenticated())
        //         .httpBasic(basic -> basic.disable())
        //         .formLogin(form -> form.disable())
        //         .exceptionHandling(ex -> ex.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
        //         .addFilterBefore(new JwtFilter(jwtUtil, authUseCase), UsernamePasswordAuthenticationFilter.class);

        // --- Nueva versión con CORS activo y soporte para Angular / Firebase: ---
        http.cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Permitir preflight OPTIONS para navegadores (Web / Firebase / Angular)
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers("/auth/login", "/error").permitAll()
                        // Los permisos por rol van en cada endpoint (@EsStaff, @EsAdmin, @EsEstudiante)
                        .anyRequest().authenticated())
                .httpBasic(basic -> basic.disable())
                .formLogin(form -> form.disable())
                .exceptionHandling(ex -> ex.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
                .addFilterBefore(new JwtFilter(jwtUtil, authUseCase), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    /**
     * Configuración global de CORS para permitir peticiones desde la Web (Angular) tanto en local como en Firebase Hosting.
     */
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        // Orígenes permitidos: desarrollo local y subdominios de Firebase Hosting
        config.setAllowedOriginPatterns(List.of(
                "http://localhost:4200",
                "http://127.0.0.1:4200",
                "https://*.web.app",
                "https://*.firebaseapp.com"
        ));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("Authorization", "Content-Type", "Accept", "Origin", "X-Requested-With"));
        config.setExposedHeaders(List.of("Authorization"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
