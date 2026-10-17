package com.example.techmentor.bean.entity;

import java.io.Serializable;
import jakarta.persistence.*;
import lombok.*;

/** Entidad JPA de la tabla `profesion_cursos`: cursos que exige cada puesto (regla de recomendación). */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "profesion_cursos")
public class ProfesionCurso implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_profesion_curso")
    private Long idProfesionCurso;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_profesion", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Profesion profesion;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_curso", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Curso curso;

    @Builder.Default
    @Column(name = "prioridad", nullable = false)
    private Integer prioridad = 2;

    @Builder.Default
    @Column(name = "obligatorio", nullable = false)
    private Boolean obligatorio = true;

}
