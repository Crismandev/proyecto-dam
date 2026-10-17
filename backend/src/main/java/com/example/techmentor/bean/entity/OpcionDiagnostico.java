package com.example.techmentor.bean.entity;

import java.io.Serializable;
import jakarta.persistence.*;
import lombok.*;

/** Entidad JPA de opcion diagnostico. */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "opciones_diagnostico")
public class OpcionDiagnostico implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_opcion_diagnostico")
    private Long idOpcionDiagnostico;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_pregunta", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private PreguntaDiagnostico pregunta;

    @Column(name = "texto", nullable = false, columnDefinition = "TEXT")
    private String texto;

    @Builder.Default
    @Column(name = "es_correcta", nullable = false)
    private Boolean esCorrecta = false;

    @Column(name = "orden")
    private Integer orden;
}
