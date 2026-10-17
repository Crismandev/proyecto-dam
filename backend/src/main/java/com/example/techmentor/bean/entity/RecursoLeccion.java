package com.example.techmentor.bean.entity;

import java.io.Serializable;
import java.time.OffsetDateTime;
import jakarta.persistence.*;
import lombok.*;

/** Entidad JPA de la tabla `recursos_leccion`: videos (YouTube u otros), textos y enlaces que forman el contenido de una lección. */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "recursos_leccion")
public class RecursoLeccion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_recurso")
    private Long idRecurso;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_leccion", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Leccion leccion;

    @Column(name = "tipo", nullable = false, length = 20)
    private String tipo;

    @Column(name = "titulo", nullable = false, length = 200)
    private String titulo;

    @Column(name = "url", length = 1000)
    private String url;

    @Column(name = "contenido", columnDefinition = "TEXT")
    private String contenido;

    @Column(name = "plataforma", length = 30)
    private String plataforma;

    @Column(name = "duracion_segundos")
    private Integer duracionSegundos;

    @Column(name = "orden", nullable = false)
    private Integer orden;

    @Builder.Default
    @Column(name = "activo", nullable = false)
    private Boolean activo = true;

    @Column(name = "fecha_creacion", insertable = false, updatable = false)
    private OffsetDateTime fechaCreacion;

}
