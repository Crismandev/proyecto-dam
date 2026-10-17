package com.example.techmentor.bean.entity;

import java.io.Serializable;
import jakarta.persistence.*;
import lombok.*;

/** Entidad JPA de examen diagnostico. */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "examenes_diagnostico")
public class ExamenDiagnostico implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_examen")
    private Long idExamen;

    // Nullable: un examen puede ser global (sin curso)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_curso")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Curso curso;

    // Nullable: examen adaptado a un puesto (profesión); null = examen general
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_profesion")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Profesion profesion;

    @Column(name = "nombre", nullable = false, length = 150)
    private String nombre;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    @Builder.Default
    @Column(name = "activo", nullable = false)
    private Boolean activo = true;
}
