package com.example.techmentor.bean.entity;

import java.io.Serializable;
import jakarta.persistence.*;
import lombok.*;

/** Entidad JPA de rol. */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "roles")
public class Rol implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_rol")
    private Long idRol;

    @Column(name = "nombre", nullable = false, unique = true, length = 50)
    private String nombre;
}
