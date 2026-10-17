package com.example.techmentor.bean.entity;

import java.io.Serializable;
import jakarta.persistence.*;
import lombok.*;

/** Entidad JPA de categoria pregunta. */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "categorias_pregunta")
public class CategoriaPregunta implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_categoria_pregunta")
    private Long idCategoriaPregunta;

    @Column(name = "nombre", nullable = false, unique = true, length = 100)
    private String nombre;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;
}
