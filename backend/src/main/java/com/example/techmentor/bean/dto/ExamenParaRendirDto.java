package com.example.techmentor.bean.dto;

import java.io.Serializable;
import lombok.*;
import java.util.List;

/** DTO de examen para rendir: datos que viajan entre la API y el cliente. */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExamenParaRendirDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private ExamenDiagnosticoDto examen;
    private List<PreguntaPublicaDto> preguntas;
}
