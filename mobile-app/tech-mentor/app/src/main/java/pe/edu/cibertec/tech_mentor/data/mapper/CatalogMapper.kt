package pe.edu.cibertec.tech_mentor.data.mapper

import pe.edu.cibertec.tech_mentor.data.remote.dto.catalog.CategoryResponseDto
import pe.edu.cibertec.tech_mentor.data.remote.dto.catalog.CourseResponseDto
import pe.edu.cibertec.tech_mentor.domain.model.Category
import pe.edu.cibertec.tech_mentor.domain.model.Course

fun CategoryResponseDto.toDomain(): Category {
    return Category(
        idCategoria = this.idCategoria,
        nombre = this.nombre,
        descripcion = this.descripcion.orEmpty(),
        icono = this.icono,
        activo = this.activo ?: true,
        fechaCreacion = this.fechaCreacion
    )
}

fun CourseResponseDto.toDomain(): Course {
    return Course(
        idCurso = this.idCurso,
        idCategoria = this.idCategoria,
        nombre = this.nombre,
        descripcion = this.descripcion.orEmpty(),
        icono = this.icono,
        xpRequerido = this.xpRequerido ?: 0,
        xpRecompensa = this.xpRecompensa ?: 0,
        activo = this.activo ?: true,
        fechaCreacion = this.fechaCreacion,
        fechaActualizacion = this.fechaActualizacion
    )
}