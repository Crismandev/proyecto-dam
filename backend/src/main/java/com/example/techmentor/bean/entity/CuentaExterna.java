package com.example.techmentor.bean.entity;

import java.io.Serializable;
import java.time.OffsetDateTime;
import jakarta.persistence.*;
import lombok.*;

/** Entidad JPA de `cuentas_externas`: cuentas de Google o GitHub vinculadas a un usuario. */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "cuentas_externas")
public class CuentaExterna implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cuenta_externa")
    private Long idCuentaExterna;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_usuario", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Usuario usuario;

    /** GOOGLE o GITHUB. */
    @Column(name = "proveedor", nullable = false, length = 20)
    private String proveedor;

    @Column(name = "id_externo", nullable = false, length = 150)
    private String idExterno;

    @Column(name = "email_externo", length = 150)
    private String emailExterno;

    @Column(name = "nombre_externo", length = 200)
    private String nombreExterno;

    @Column(name = "avatar_url", length = 500)
    private String avatarUrl;

    @Builder.Default
    @Column(name = "fecha_vinculacion", nullable = false)
    private OffsetDateTime fechaVinculacion = OffsetDateTime.now();

    @Column(name = "ultimo_acceso")
    private OffsetDateTime ultimoAcceso;
}
