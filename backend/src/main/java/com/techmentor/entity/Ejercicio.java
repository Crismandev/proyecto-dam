package com.techmentor.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ejercicios")
@Getter
@Setter
public class Ejercicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ejercicio")
    private Long idEjercicio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_leccion", nullable = false)
    private Leccion leccion;

    // En la nueva BD tipo_ejercicio es VARCHAR(50) sin ENUM constraint en BD,
    // lo manejamos como String para flexibilidad
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

    @Column(name = "xp_recompensa", nullable = false)
    private Integer xpRecompensa = 0;

    @OneToMany(mappedBy = "ejercicio", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OpcionEjercicio> opciones = new ArrayList<>();
}
