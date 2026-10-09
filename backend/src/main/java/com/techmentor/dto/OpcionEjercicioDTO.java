package com.techmentor.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OpcionEjercicioDTO {
    private Long idOpcion;
    private String texto;
    private Boolean esCorrecta;
    private Integer orden;

    public String getTextoOpcion() {
        return texto;
    }
}
