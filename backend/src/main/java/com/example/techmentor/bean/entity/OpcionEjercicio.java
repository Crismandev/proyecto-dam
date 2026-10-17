package com.example.techmentor.bean.entity;

import java.io.Serializable;
import jakarta.persistence.*;
import lombok.*;

/** Entidad JPA de la tabla `opciones_ejercicio`: opciones de un ejercicio de opción múltiple (incluye es_correcta: solo lo ve el staff). */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "opciones_ejercicio")
public class OpcionEjercicio implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_opcion")
    private Long idOpcion;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_ejercicio", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Ejercicio ejercicio;

    @Column(name = "texto", nullable = false, columnDefinition = "TEXT")
    private String texto;

    @Builder.Default
    @Column(name = "es_correcta", nullable = false)
    private Boolean esCorrecta = false;

    @Column(name = "orden")
    private Integer orden;

}
