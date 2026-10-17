package com.example.techmentor.bean.entity;

/** Valores permitidos por los CHECK de progreso_curso y progreso_leccion. */
public enum EstadoProgreso {
    NO_INICIADO,
    EN_CURSO,
    COMPLETADO,
    ABANDONADO   // solo aplica a progreso_curso
}
