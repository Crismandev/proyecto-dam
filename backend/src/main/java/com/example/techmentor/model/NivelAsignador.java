package com.example.techmentor.model;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import com.example.techmentor.bean.entity.Nivel;
import com.example.techmentor.config.DiagnosticoProperties;
import com.example.techmentor.persistence.NivelRepository;

/**
 * Decide qué nivel de un curso corresponde a un porcentaje de puntaje.
 * Las bandas se configuran en application.yml (techmentor.diagnostico.bandas).
 * Si el curso tiene menos niveles que el orden objetivo, se asigna el más alto disponible.
 */
@Component
@RequiredArgsConstructor
public class NivelAsignador {

    private final NivelRepository nivelRepository;
    private final DiagnosticoProperties properties;

    public Optional<Nivel> asignar(Long idCurso, double porcentaje) {
        List<Nivel> niveles = nivelRepository.findByCurso_IdCursoOrderByOrdenAsc(idCurso);
        if (niveles.isEmpty()) {
            return Optional.empty();
        }

        int ordenObjetivo = properties.getBandas().stream()
                .filter(b -> porcentaje >= b.getDesdePorcentaje())
                .max(Comparator.comparingDouble(DiagnosticoProperties.Banda::getDesdePorcentaje))
                .map(DiagnosticoProperties.Banda::getOrdenNivel)
                .orElse(1);

        // Niveles vienen ordenados ASC: se toma el último con orden <= objetivo
        Nivel elegido = niveles.get(0);
        for (Nivel n : niveles) {
            if (n.getOrden() <= ordenObjetivo) {
                elegido = n;
            }
        }
        return Optional.of(elegido);
    }
}
