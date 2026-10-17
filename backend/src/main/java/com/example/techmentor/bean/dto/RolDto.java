package com.example.techmentor.bean.dto;

import java.io.Serializable;
import jakarta.validation.constraints.*;
import lombok.*;

/** DTO de rol: datos que viajan entre la API y el cliente. */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RolDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long idRol;

    @NotBlank
    @Size(max = 50)
    private String nombre;

}
