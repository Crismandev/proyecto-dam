package com.example.techmentor.bean.entity;

import java.io.Serializable;
import java.time.OffsetDateTime;
import jakarta.persistence.*;
import lombok.*;

/** Entidad JPA de `recompensas_usuario`: recompensas que obtuvo un usuario y si ya las reclamó. */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "recompensas_usuario")
public class RecompensaUsuario implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_recompensa_usuario")
    private Long idRecompensaUsuario;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_usuario", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_recompensa", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Recompensa recompensa;

    @Builder.Default
    @Column(name = "fecha_obtencion", nullable = false)
    private OffsetDateTime fechaObtencion = OffsetDateTime.now();

    @Builder.Default
    @Column(name = "reclamada", nullable = false)
    private Boolean reclamada = false;
}
