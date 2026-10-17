package com.example.techmentor.bean.dto;

import java.io.Serializable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

/** DTO para que el usuario autenticado cambie su contraseña. */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CambiarClaveDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank
    private String claveActual;

    @NotBlank
    @Size(min = 8, max = 100)
    private String claveNueva;
}
