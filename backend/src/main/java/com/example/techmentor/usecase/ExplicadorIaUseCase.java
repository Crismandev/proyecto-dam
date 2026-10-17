package com.example.techmentor.usecase;

import java.util.List;
import java.util.Map;
import com.example.techmentor.bean.dto.AnalisisCategoriaDto;
import com.example.techmentor.bean.entity.RecomendacionCurso;

/**
 * Punto de extensión para IA: devuelve una explicación breve por curso recomendado
 * (clave = idCurso). Hoy está deshabilitado (ver ExplicadorIaDeshabilitadoModel).
 * Para activarla: crea otra implementación anotada con
 * {@code @ConditionalOnProperty(prefix = "techmentor.ia", name = "habilitada", havingValue = "true")}
 * y pon {@code techmentor.ia.habilitada: true} en application.yml.
 */
public interface ExplicadorIaUseCase {

    Map<Long, String> explicar(Long idUsuario, List<AnalisisCategoriaDto> analisis,
                               List<RecomendacionCurso> recomendaciones);
}
