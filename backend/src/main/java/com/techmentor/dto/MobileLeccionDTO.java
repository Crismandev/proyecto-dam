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
public class MobileLeccionDTO {
    private Long idLeccion;
    private String titulo;
    private String descripcion;
    private Integer totalEjercicios;
    private List<MobileEjercicioDTO> ejercicios;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MobileEjercicioDTO {
        private Long idEjercicio;
        private String tipoEjercicio;
        private String enunciado;
        private Integer orden;
        private List<MobileOpcionDTO> opciones; // Solo para OPCION_MULTIPLE
        private String idEntrevista; // Identificador virtual si es tipo ENTREVISTA
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MobileOpcionDTO {
        private Long idOpcion;
        private String texto;
    }
}
