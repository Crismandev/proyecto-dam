package com.example.techmentor.bean.entity;

import java.io.Serializable;
import jakarta.persistence.*;
import lombok.*;

/** Entidad JPA de pregunta diagnostico. */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "preguntas_diagnostico")
public class PreguntaDiagnostico implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pregunta")
    private Long idPregunta;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_examen", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private ExamenDiagnostico examen;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_categoria_pregunta", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private CategoriaPregunta categoriaPregunta;

    @Column(name = "enunciado", nullable = false, columnDefinition = "TEXT")
    private String enunciado;

    @Column(name = "nivel_dificultad", length = 30)
    private String nivelDificultad;

    @Column(name = "tipo_pregunta", nullable = false, length = 50)
    private String tipoPregunta;

    @Builder.Default
    @Column(name = "puntaje", nullable = false)
    private Integer puntaje = 1;

    @Column(name = "orden", nullable = false)
    private Integer orden;
}
