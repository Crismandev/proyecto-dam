package com.example.techmentor.bean.entity;

import java.io.Serializable;
import java.time.OffsetDateTime;
import jakarta.persistence.*;
import lombok.*;

/** Entidad JPA de `recomendaciones_curso`: cursos recomendados a un usuario (por reglas de BD o por IA). */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "recomendaciones_curso")
public class RecomendacionCurso implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_recomendacion")
    private Long idRecomendacion;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_usuario", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "id_resultado", nullable = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private ResultadoDiagnostico resultado;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_curso", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Curso curso;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "id_profesion", nullable = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Profesion profesion;

    @Builder.Default
    @Column(name = "prioridad", nullable = false)
    private Integer prioridad = 2;

    @Column(name = "motivo", nullable = false, columnDefinition = "TEXT")
    private String motivo;

    /** REGLAS (BD) o IA. */
    @Builder.Default
    @Column(name = "origen", nullable = false, length = 20)
    private String origen = "REGLAS";

    /** Explicación breve redactada por IA (null mientras la IA esté deshabilitada). */
    @Column(name = "explicacion_ia", columnDefinition = "TEXT")
    private String explicacionIa;

    /** PENDIENTE, INICIADA o DESCARTADA. */
    @Builder.Default
    @Column(name = "estado", nullable = false, length = 20)
    private String estado = "PENDIENTE";

    @Builder.Default
    @Column(name = "fecha_creacion", nullable = false)
    private OffsetDateTime fechaCreacion = OffsetDateTime.now();
}
