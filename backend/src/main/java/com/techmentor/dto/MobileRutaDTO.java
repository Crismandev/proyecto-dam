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
public class MobileRutaDTO {
    private Long idCurso;
    private String nombreCurso;
    private Integer porcentajeTotal;
    private List<NivelRutaDTO> niveles;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NivelRutaDTO {
        private Long idNivel;
        private String titulo;
        private Integer orden;
        private List<LeccionRutaDTO> lecciones;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LeccionRutaDTO {
        private Long idLeccion;
        private String titulo;
        private Integer orden;
        private Boolean completada;
        private Boolean bloqueada;
        private Integer estrellas;
    }
}
