package com.techmentor.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "niveles")
@Getter
@Setter
public class Nivel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_nivel")
    private Long idNivel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_curso", nullable = false)
    private Curso curso;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "orden", nullable = false)
    private Integer orden;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    @OneToMany(mappedBy = "nivel", cascade = CascadeType.ALL)
    private java.util.List<Leccion> lecciones = new java.util.ArrayList<>();

    public String getNombreNivel() {
        return nombre;
    }

    public void setNombreNivel(String nombreNivel) {
        this.nombre = nombreNivel;
    }
}
