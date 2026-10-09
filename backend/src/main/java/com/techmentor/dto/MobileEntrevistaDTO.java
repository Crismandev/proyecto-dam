package com.techmentor.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MobileEntrevistaDTO {
    private Long idEjercicio;
    private String inputEstudiante; // Opcional, si queremos validar su input contra algo.
    private List<String> tips; // Explicación separada en tips
    private String respuestaSugerida;
}
