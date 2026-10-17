package com.example.techmentor.bean.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import lombok.*;

/** DTO de lectura de un usuario (nunca incluye la contraseña). */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UsuarioDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long idUsuario;
    private Long idRol;
    private String nombreRol;
    private Long idEstadoUsuario;
    private String nombreEstado;
    private String nombres;
    private String apellidos;
    private String email;
    private Boolean emailVerificado;
    private LocalDate fechaNacimiento;
    private String avatarUrl;
    private String biografia;
    private String telefono;
    private String pais;
    private String ciudad;
    private Integer xpTotal;
    private Integer monedas;
    private Integer nivelUsuario;
    private Integer rachaActual;
    private Integer rachaMaxima;
    private OffsetDateTime fechaRegistro;
}
