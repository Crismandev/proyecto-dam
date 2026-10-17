package com.example.techmentor.bean.entity;

import java.io.Serializable;
import java.time.OffsetDateTime;
import jakarta.persistence.*;
import lombok.*;

/** Entidad JPA de `verificaciones_email`: códigos (con hash) enviados por correo para verificar o recuperar clave. */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "verificaciones_email")
public class VerificacionEmail implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_verificacion")
    private Long idVerificacion;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_usuario", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Usuario usuario;

    @Column(name = "email", nullable = false, length = 150)
    private String email;

    @Column(name = "codigo_hash", nullable = false, length = 255)
    @ToString.Exclude
    private String codigoHash;

    /** VERIFICAR_EMAIL o RECUPERAR_CLAVE. */
    @Builder.Default
    @Column(name = "tipo", nullable = false, length = 30)
    private String tipo = "VERIFICAR_EMAIL";

    @Builder.Default
    @Column(name = "intentos", nullable = false)
    private Integer intentos = 0;

    @Builder.Default
    @Column(name = "usado", nullable = false)
    private Boolean usado = false;

    @Builder.Default
    @Column(name = "fecha_creacion", nullable = false)
    private OffsetDateTime fechaCreacion = OffsetDateTime.now();

    @Column(name = "fecha_expiracion", nullable = false)
    private OffsetDateTime fechaExpiracion;

    @Column(name = "fecha_uso")
    private OffsetDateTime fechaUso;
}
