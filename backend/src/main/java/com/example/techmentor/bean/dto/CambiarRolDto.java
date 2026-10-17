package com.example.techmentor.bean.dto;

import java.io.Serializable;
import jakarta.validation.constraints.NotNull;
import lombok.*;

/** DTO para cambiar el rol de un usuario. */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CambiarRolDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    private Long idRol;
}
