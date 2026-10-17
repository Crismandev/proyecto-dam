package com.example.techmentor.bean.entity;

import java.io.Serializable;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.OffsetDateTime;

/** Entidad JPA de usuario. */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "usuarios")
public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long idUsuario;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_rol", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Rol rol;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_estado_usuario", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private EstadoUsuario estadoUsuario;

    @Column(name = "nombres", nullable = false, length = 100)
    private String nombres;

    @Column(name = "apellidos", nullable = false, length = 100)
    private String apellidos;

    @Column(name = "email", nullable = false, unique = true, length = 150)
    private String email;

    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento;

    @Column(name = "avatar_url", length = 500)
    private String avatarUrl;

    @Column(name = "biografia", columnDefinition = "TEXT")
    private String biografia;

    @Column(name = "telefono", length = 30)
    private String telefono;

    @Column(name = "pais", length = 100)
    private String pais;

    @Column(name = "ciudad", length = 100)
    private String ciudad;

    @Builder.Default
    @Column(name = "xp_total", nullable = false)
    private Integer xpTotal = 0;

    @Builder.Default
    @Column(name = "monedas", nullable = false)
    private Integer monedas = 0;

    @Builder.Default
    @Column(name = "nivel_usuario", nullable = false)
    private Integer nivelUsuario = 1;

    @Builder.Default
    @Column(name = "racha_actual", nullable = false)
    private Integer rachaActual = 0;

    @Builder.Default
    @Column(name = "racha_maxima", nullable = false)
    private Integer rachaMaxima = 0;

    @Column(name = "fecha_inicio_racha")
    private LocalDate fechaInicioRacha;

    @Column(name = "ultima_actividad_racha")
    private LocalDate ultimaActividadRacha;

    /** true cuando el correo se verificó (código por email o login con Google/GitHub). */
    @Builder.Default
    @Column(name = "email_verificado", nullable = false)
    private Boolean emailVerificado = false;

    @Column(name = "fecha_verificacion_email")
    private OffsetDateTime fechaVerificacionEmail;

    // Lo maneja la BD (DEFAULT y trigger)
    @Column(name = "fecha_registro", insertable = false, updatable = false)
    private OffsetDateTime fechaRegistro;

    @Column(name = "fecha_actualizacion", insertable = false, updatable = false)
    private OffsetDateTime fechaActualizacion;
}
