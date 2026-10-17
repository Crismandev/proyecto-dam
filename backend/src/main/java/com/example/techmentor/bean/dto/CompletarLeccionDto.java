package com.example.techmentor.bean.dto;

// import jakarta.validation.constraints.Min;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;
import lombok.*;

/** DTO de completar leccion: datos que viajan entre la API y el cliente. */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompletarLeccionDto implements Serializable {

    private static final long serialVersionUID = 1L;

    // Versión anterior: el cliente decidía el puntaje de la lección.
    // @NotNull
    // @Min(0)
    // private Integer puntaje;

    /** Una respuesta por ejercicio. El servidor calcula un punto por acierto. */
    @NotNull
    @Valid
    private List<@NotNull RespuestaLeccionDto> respuestas;
}
