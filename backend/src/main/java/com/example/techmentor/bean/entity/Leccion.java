package com.example.techmentor.bean.entity;

import java.io.Serializable;
import jakarta.persistence.*;
import lombok.*;

/** Entidad JPA de la tabla `lecciones`: lecciones de un nivel (texto base de la lección). */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "lecciones")
public class Leccion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_leccion")
    private Long idLeccion;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_nivel", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Nivel nivel;

    @Column(name = "titulo", nullable = false, length = 150)
    private String titulo;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "contenido", columnDefinition = "TEXT")
    private String contenido;

    @Column(name = "orden", nullable = false)
    private Integer orden;

    @Builder.Default
    @Column(name = "xp_recompensa", nullable = false)
    private Integer xpRecompensa = 0;

}
