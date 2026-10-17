package com.example.techmentor.bean.entity;

import java.io.Serializable;
import java.time.OffsetDateTime;
import jakarta.persistence.*;
import lombok.*;

/** Entidad JPA de `usuario_profesiones`: profesiones (puestos) a los que apunta un usuario; una sola principal. */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "usuario_profesiones")
public class UsuarioProfesion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario_profesion")
    private Long idUsuarioProfesion;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_usuario", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_profesion", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Profesion profesion;

    @Builder.Default
    @Column(name = "principal", nullable = false)
    private Boolean principal = false;

    @Builder.Default
    @Column(name = "fecha_asignacion", nullable = false)
    private OffsetDateTime fechaAsignacion = OffsetDateTime.now();
}
