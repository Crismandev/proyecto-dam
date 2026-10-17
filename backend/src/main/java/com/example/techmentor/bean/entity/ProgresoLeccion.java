package com.example.techmentor.bean.entity;

import java.io.Serializable;
import java.time.OffsetDateTime;
import jakarta.persistence.*;
import lombok.*;

/** Entidad JPA de progreso leccion. */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "progreso_leccion")
public class ProgresoLeccion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_progreso_leccion")
    private Long idProgresoLeccion;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_usuario", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_leccion", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Leccion leccion;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    @Column(name = "estado", nullable = false, length = 30)
    private EstadoProgreso estado = EstadoProgreso.NO_INICIADO;

    @Builder.Default
    @Column(name = "puntaje", nullable = false)
    private Integer puntaje = 0;

    @Builder.Default
    @Column(name = "intentos", nullable = false)
    private Integer intentos = 0;

    @Column(name = "fecha_inicio")
    private OffsetDateTime fechaInicio;

    @Column(name = "fecha_ultima_actividad")
    private OffsetDateTime fechaUltimaActividad;

    @Column(name = "fecha_completado")
    private OffsetDateTime fechaCompletado;

    @Builder.Default
    @Column(name = "xp_ganado", nullable = false)
    private Integer xpGanado = 0;
}
