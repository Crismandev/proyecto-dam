package com.example.techmentor.bean.dto;

import java.io.Serializable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

/** DTO para cambiar el estado de un usuario (queda registrado en el historial). */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CambiarEstadoDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    private Long idEstadoUsuario;

    @Size(max = 255)
    private String motivo;
}
