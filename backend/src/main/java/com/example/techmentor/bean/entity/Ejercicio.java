package com.example.techmentor.bean.entity;

import java.io.Serializable;
import jakarta.persistence.*;
import lombok.*;

/** Entidad JPA de la tabla `ejercicios`: ejercicios de una lección (incluye la respuesta correcta: solo lo ve el staff). */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "ejercicios")
public class Ejercicio implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ejercicio")
    private Long idEjercicio;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_leccion", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Leccion leccion;

    @Column(name = "tipo_ejercicio", nullable = false, length = 50)
    private String tipoEjercicio;

    @Column(name = "enunciado", nullable = false, columnDefinition = "TEXT")
    private String enunciado;

    @Column(name = "orden", nullable = false)
    private Integer orden;

    @Column(name = "respuesta_correcta", columnDefinition = "TEXT")
    private String respuestaCorrecta;

    @Column(name = "explicacion", columnDefinition = "TEXT")
    private String explicacion;

    @Builder.Default
    @Column(name = "xp_recompensa", nullable = false)
    private Integer xpRecompensa = 0;

}
