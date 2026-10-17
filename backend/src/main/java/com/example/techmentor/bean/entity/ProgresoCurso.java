package com.example.techmentor.bean.entity;

import java.io.Serializable;
import java.time.OffsetDateTime;
import jakarta.persistence.*;
import lombok.*;

/** Entidad JPA de progreso curso. */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "progreso_curso")
public class ProgresoCurso implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_progreso")
    private Long idProgreso;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_usuario", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_curso", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Curso curso;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_nivel_actual")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Nivel nivelActual;

    @Column(name = "fecha_inicio")
    private OffsetDateTime fechaInicio;

    @Column(name = "fecha_ultima_actividad")
    private OffsetDateTime fechaUltimaActividad;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    @Column(name = "estado", nullable = false, length = 30)
    private EstadoProgreso estado = EstadoProgreso.NO_INICIADO;

    @Builder.Default
    @Column(name = "xp_ganado", nullable = false)
    private Integer xpGanado = 0;

    @Column(name = "fecha_completado")
    private OffsetDateTime fechaCompletado;
}
