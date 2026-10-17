package com.example.techmentor.bean.entity;

import java.io.Serializable;
import jakarta.persistence.*;
import lombok.*;

/** Entidad JPA de la tabla `recompensas`: recompensas que el usuario puede obtener (XP o monedas). */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "recompensas")
public class Recompensa implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_recompensa")
    private Long idRecompensa;

    @Column(name = "nombre", nullable = false, unique = true, length = 150)
    private String nombre;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "tipo", nullable = false, length = 50)
    private String tipo;

    @Builder.Default
    @Column(name = "cantidad", nullable = false)
    private Integer cantidad = 0;

    @Builder.Default
    @Column(name = "costo_monedas", nullable = false)
    private Integer costoMonedas = 0;

    @Column(name = "dia_secuencia_requerido")
    private Integer diaSecuenciaRequerido;

    @Builder.Default
    @Column(name = "activa", nullable = false)
    private Boolean activa = true;

}
