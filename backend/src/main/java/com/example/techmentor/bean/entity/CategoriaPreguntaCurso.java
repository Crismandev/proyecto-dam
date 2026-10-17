package com.example.techmentor.bean.entity;

import java.io.Serializable;
import jakarta.persistence.*;
import lombok.*;

/** Entidad JPA de la tabla `categoria_pregunta_cursos`: curso que refuerza una categoría de pregunta y % mínimo esperado (regla de recomendación). */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "categoria_pregunta_cursos")
public class CategoriaPreguntaCurso implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_categoria_pregunta_curso")
    private Long idCategoriaPreguntaCurso;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_categoria_pregunta", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private CategoriaPregunta categoriaPregunta;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_curso", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Curso curso;

    @Builder.Default
    @Column(name = "umbral_porcentaje", nullable = false)
    private Integer umbralPorcentaje = 60;

}
