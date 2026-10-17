package com.example.techmentor.bean.dto;

import java.io.Serializable;
import lombok.*;

/** Vista para quien rinde el examen: NO incluye esCorrecta. */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OpcionPublicaDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long idOpcionDiagnostico;
    private String texto;
    private Integer orden;
}
