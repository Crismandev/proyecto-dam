package com.example.techmentor.bean.entity;

import java.io.Serializable;
import jakarta.persistence.*;
import lombok.*;
import java.time.OffsetDateTime;

/** Entidad JPA de resultado diagnostico. */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "resultados_diagnostico")
public class ResultadoDiagnostico implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_resultado")
    private Long idResultado;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_usuario", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_examen", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private ExamenDiagnostico examen;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_nivel_asignado")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Nivel nivelAsignado;

    @Builder.Default
    @Column(name = "puntaje_obtenido", nullable = false)
    private Integer puntajeObtenido = 0;

    @Builder.Default
    @Column(name = "puntaje_maximo", nullable = false)
    private Integer puntajeMaximo = 0;

    @Builder.Default
    @Column(name = "fecha_realizacion", nullable = false)
    private OffsetDateTime fechaRealizacion = OffsetDateTime.now();
}
