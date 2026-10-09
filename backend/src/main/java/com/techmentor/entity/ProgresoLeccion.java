package com.techmentor.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.OffsetDateTime;

@Entity
@Table(name = "progreso_leccion")
@Getter
@Setter
public class ProgresoLeccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_progreso_leccion")
    private Long idProgresoLeccion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_leccion", nullable = false)
    private Leccion leccion;

    @Column(name = "estado", nullable = false, length = 30)
    private String estado = "NO_INICIADO";

    @Column(name = "puntaje", nullable = false)
    private Integer puntaje = 0;

    @Column(name = "intentos", nullable = false)
    private Integer intentos = 0;

    @Column(name = "fecha_inicio")
    private OffsetDateTime fechaInicio;

    @Column(name = "fecha_ultima_actividad")
    private OffsetDateTime fechaUltimaActividad;

    @Column(name = "fecha_completado")
    private OffsetDateTime fechaCompletado;

    @Column(name = "xp_ganado", nullable = false)
    private Integer xpGanado = 0;

    public Long getIdProgreso() {
        return idProgresoLeccion;
    }
}
