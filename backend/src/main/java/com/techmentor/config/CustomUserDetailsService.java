package com.techmentor.config;

import com.techmentor.entity.CredencialUsuario;
import com.techmentor.repository.CredencialUsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final CredencialUsuarioRepository credencialRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {
        CredencialUsuario credencial = credencialRepository.findByUsername(identifier)
                .or(() -> credencialRepository.findByUsuarioEmail(identifier))
                .orElseThrow(() -> new UsernameNotFoundException(
                        "No se encontró un usuario con identificador: " + identifier));

        String nombreRol = credencial.getUsuario().getRol() != null
                ? credencial.getUsuario().getRol().getNombre()
                : "ALUMNO";

        String authority = nombreRol.startsWith("ROLE_") ? nombreRol : "ROLE_" + nombreRol;

        return new User(
                credencial.getUsername(),
                credencial.getPasswordHash(),
                Collections.singletonList(new SimpleGrantedAuthority(authority))
        );
    }
}
