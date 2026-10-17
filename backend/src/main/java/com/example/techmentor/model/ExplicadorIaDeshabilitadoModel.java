package com.example.techmentor.model;

import java.util.List;
import java.util.Map;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import com.example.techmentor.bean.dto.AnalisisCategoriaDto;
import com.example.techmentor.bean.entity.RecomendacionCurso;
import com.example.techmentor.usecase.ExplicadorIaUseCase;

/**
 * Implementación por defecto: la IA está apagada, no se genera ninguna explicación.
 * Se usa mientras techmentor.ia.habilitada sea false (valor por defecto).
 */
@Service
@ConditionalOnProperty(prefix = "techmentor.ia", name = "habilitada", havingValue = "false", matchIfMissing = true)
public class ExplicadorIaDeshabilitadoModel implements ExplicadorIaUseCase {

    @Override
    public Map<Long, String> explicar(Long idUsuario, List<AnalisisCategoriaDto> analisis,
                                      List<RecomendacionCurso> recomendaciones) {
        return Map.of();
    }
}
