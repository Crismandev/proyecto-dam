package com.techmentor.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDTO {
    private Long idUsuario;
    private Long idRol;
    private String nombreRol;
    private String nombres;
    private String apellidos;
    private String correo;
    private LocalDate fechaNacimiento;
    private LocalDateTime fechaRegistro;
    private String avatar;
    private Integer xpTotal;
    private Integer monedas;
}
