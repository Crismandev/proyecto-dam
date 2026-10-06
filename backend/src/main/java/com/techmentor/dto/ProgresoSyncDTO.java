package com.techmentor.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProgresoSyncDTO {
    @NotNull
    private Long idLeccion;
    
    @NotNull
    private Integer xpGanado;
    
    @NotNull
    private Integer preguntasCorrectas;
    
    @NotNull
    private Integer preguntasIncorrectas;
    
    private LocalDateTime fechaCompletado; // Puede venir nulo desde el móvil, se setea en servidor
    
    private String uuidCliente; // Para evitar sincronizaciones duplicadas
}
