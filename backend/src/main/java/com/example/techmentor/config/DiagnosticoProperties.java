package com.example.techmentor.config;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Reglas para asignar el nivel tras un examen diagnóstico.
 * Se pueden cambiar en application.yml (techmentor.diagnostico.bandas) sin tocar código.
 *
 * Valores por defecto (umbrales usados con frecuencia en educación:
 * 60% = aprobado, 80% = dominio del contenido):
 *   0-39%  -> nivel de orden 1
 *   40-59% -> nivel de orden 2
 *   60-79% -> nivel de orden 3
 *   80%+   -> nivel de orden 4
 */
@Data
@Component
@ConfigurationProperties(prefix = "techmentor.diagnostico")
public class DiagnosticoProperties {

    private List<Banda> bandas = new ArrayList<>(List.of(
            new Banda(0, 1),
            new Banda(40, 2),
            new Banda(60, 3),
            new Banda(80, 4)));

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Banda {
        /** Porcentaje mínimo (inclusive) para entrar en esta banda. */
        private double desdePorcentaje;
        /** Orden del nivel del curso al que corresponde la banda. */
        private int ordenNivel;
    }
}
