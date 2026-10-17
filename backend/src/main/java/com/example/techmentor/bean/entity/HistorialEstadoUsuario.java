package com.example.techmentor.bean.entity;

import java.io.Serializable;
import java.time.OffsetDateTime;
import jakarta.persistence.*;
import lombok.*;

/** Entidad JPA de `historial_estados_usuario`: auditoría de cada cambio de estado de un usuario. */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "historial_estados_usuario")
public class HistorialEstadoUsuario implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_historial")
    private Long idHistorial;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_usuario", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_estado_usuario", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private EstadoUsuario estadoUsuario;

    @Column(name = "motivo", length = 255)
    private String motivo;

    @Builder.Default
    @Column(name = "fecha_cambio", nullable = false)
    private OffsetDateTime fechaCambio = OffsetDateTime.now();
}
