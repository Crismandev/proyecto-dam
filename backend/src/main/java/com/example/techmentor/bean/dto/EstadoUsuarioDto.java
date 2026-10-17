package com.example.techmentor.bean.dto;

import java.io.Serializable;
import jakarta.validation.constraints.*;
import lombok.*;

/** DTO de estado de usuario: datos que viajan entre la API y el cliente. */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EstadoUsuarioDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long idEstadoUsuario;

    @NotBlank
    @Size(max = 30)
    private String nombre;

}
