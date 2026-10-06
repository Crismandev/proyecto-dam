package com.techmentor.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UsuarioSaveDTO {

    @NotNull(message = "El rol es obligatorio")
    private Long idRol;

    @NotBlank(message = "Los nombres son obligatorios")
    private String nombres;

    private String apellidos;

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "Debe ser un correo válido")
    private String correo;

    private String contrasena; // Optional on edit, required on create

    private LocalDate fechaNacimiento;
    private String avatar;
}
