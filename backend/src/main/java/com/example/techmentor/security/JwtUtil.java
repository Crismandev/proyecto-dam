package com.example.techmentor.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import java.security.Key;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/** Crea y valida los JWT (firma HS256). La clave y la expiración se leen de application.yml. */
@Component
public class JwtUtil {

    /** Clave en Base64 (mínimo 256 bits). Ver application.yml. */
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration-ms:3600000}")
    private long expirationMs;

    private Key key;

    @PostConstruct
    void init() {
        key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }

    public String generarToken(Long idUsuario, String username, String rol) {
        Date ahora = new Date();
        return Jwts.builder()
                .setSubject(username)
                .claim("idUsuario", idUsuario)
                .claim("rol", rol)
                .setIssuedAt(ahora)
                .setExpiration(new Date(ahora.getTime() + expirationMs))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /** Valida firma y expiración; lanza JwtException si el token no sirve. */
    public Claims extraerClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }
}
