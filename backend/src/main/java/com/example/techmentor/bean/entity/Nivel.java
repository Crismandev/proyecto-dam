package com.example.techmentor.bean.entity;

import java.io.Serializable;
import jakarta.persistence.*;
import lombok.*;

/** Entidad JPA de nivel. */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "niveles")
public class Nivel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_nivel")
    private Long idNivel;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_curso", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Curso curso;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "orden", nullable = false)
    private Integer orden;
}
