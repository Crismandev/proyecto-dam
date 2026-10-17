package com.example.techmentor.bean.dto;

import java.io.Serializable;
import java.time.LocalDate;
import jakarta.validation.constraints.*;
import lombok.*;

/** DTO para que un ADMINISTRADOR cree un usuario (con su credencial de acceso). */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UsuarioCrearDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    private Long idRol;

    /** Opcional: si no se envía queda ACTIVO. */
    private Long idEstadoUsuario;

    @NotBlank
    @Size(max = 100)
    private String nombres;

    @NotBlank
    @Size(max = 100)
    private String apellidos;

    @NotBlank
    @Email
    @Size(max = 150)
    private String email;

    @NotBlank
    @Size(min = 3, max = 100)
    private String username;

    @NotBlank
    @Size(min = 8, max = 100)
    private String password;

    private LocalDate fechaNacimiento;
    @Size(max = 30)
    private String telefono;
    @Size(max = 100)
    private String pais;
    @Size(max = 100)
    private String ciudad;
}
