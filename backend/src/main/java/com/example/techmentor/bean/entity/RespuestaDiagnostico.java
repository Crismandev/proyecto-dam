package com.example.techmentor.bean.entity;

import java.io.Serializable;
import jakarta.persistence.*;
import lombok.*;

/** Entidad JPA de respuesta diagnostico. */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "respuestas_diagnostico")
public class RespuestaDiagnostico implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_respuesta")
    private Long idRespuesta;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_resultado", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private ResultadoDiagnostico resultado;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_pregunta", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private PreguntaDiagnostico pregunta;

    // Nullable: pregunta sin responder.
    // La BD valida con FK compuesta que la opción pertenezca a la pregunta.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_opcion_seleccionada")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private OpcionDiagnostico opcionSeleccionada;
}
