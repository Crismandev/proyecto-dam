package com.example.techmentor.bean.entity;

import java.io.Serializable;
import java.time.OffsetDateTime;
import jakarta.persistence.*;
import lombok.*;

/** Entidad JPA de `misiones_usuario`: avance de un usuario en una misión. */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "misiones_usuario")
public class MisionUsuario implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_mision_usuario")
    private Long idMisionUsuario;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_usuario", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_mision", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Mision mision;

    @Builder.Default
    @Column(name = "progreso_actual", nullable = false)
    private Integer progresoActual = 0;

    @Builder.Default
    @Column(name = "completada", nullable = false)
    private Boolean completada = false;

    @Builder.Default
    @Column(name = "fecha_asignacion", nullable = false)
    private OffsetDateTime fechaAsignacion = OffsetDateTime.now();

    @Column(name = "fecha_completada")
    private OffsetDateTime fechaCompletada;
}
