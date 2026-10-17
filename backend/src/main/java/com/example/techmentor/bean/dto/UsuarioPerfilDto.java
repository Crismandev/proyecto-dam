package com.example.techmentor.bean.dto;

import java.io.Serializable;
import java.time.LocalDate;
import jakarta.validation.constraints.Size;
import lombok.*;

/** DTO para editar el perfil: los campos que lleguen null no se modifican. */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UsuarioPerfilDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @Size(max = 100)
    private String nombres;
    @Size(max = 100)
    private String apellidos;
    private LocalDate fechaNacimiento;
    @Size(max = 500)
    private String avatarUrl;
    private String biografia;
    @Size(max = 30)
    private String telefono;
    @Size(max = 100)
    private String pais;
    @Size(max = 100)
    private String ciudad;
}
