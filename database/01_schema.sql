-- ==========================================
-- TechMentor Database Schema (01_schema.sql)
-- ==========================================

CREATE EXTENSION IF NOT EXISTS pgcrypto;

-- 1. Roles
CREATE TABLE roles (
    id_rol BIGSERIAL PRIMARY KEY,
    nombre_rol VARCHAR(50) NOT NULL UNIQUE,
    descripcion VARCHAR(255)
);

-- 2. Usuarios
CREATE TABLE usuarios (
    id_usuario BIGSERIAL PRIMARY KEY,
    id_rol BIGINT NOT NULL REFERENCES roles(id_rol) ON DELETE RESTRICT,
    nombres VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100),
    correo VARCHAR(150) NOT NULL UNIQUE,
    contrasena VARCHAR(255) NOT NULL,
    fecha_nacimiento DATE,
    fecha_registro TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    avatar VARCHAR(255),
    xp_total INT NOT NULL DEFAULT 0,
    monedas INT NOT NULL DEFAULT 0
);
CREATE INDEX idx_usuarios_id_rol ON usuarios(id_rol);

-- 3. Categorias
CREATE TABLE categorias (
    id_categoria BIGSERIAL PRIMARY KEY,
    nombre_categoria VARCHAR(100) NOT NULL UNIQUE,
    descripcion TEXT,
    icono VARCHAR(100)
);

-- 4. Cursos
CREATE TABLE cursos (
    id_curso BIGSERIAL PRIMARY KEY,
    id_categoria BIGINT NOT NULL REFERENCES categorias(id_categoria) ON DELETE RESTRICT,
    nombre_curso VARCHAR(150) NOT NULL,
    descripcion TEXT,
    icono VARCHAR(100),
    dificultad VARCHAR(20) NOT NULL CHECK (dificultad IN ('PRINCIPIANTE', 'INTERMEDIO', 'AVANZADO')),
    xp_requerido INT NOT NULL DEFAULT 0,
    xp_recompensa INT NOT NULL DEFAULT 0
);
CREATE INDEX idx_cursos_id_categoria ON cursos(id_categoria);

-- 5. Niveles
CREATE TABLE niveles (
    id_nivel BIGSERIAL PRIMARY KEY,
    id_curso BIGINT NOT NULL REFERENCES cursos(id_curso) ON DELETE CASCADE,
    nombre_nivel VARCHAR(100) NOT NULL,
    orden INT NOT NULL,
    descripcion TEXT
);
CREATE INDEX idx_niveles_id_curso ON niveles(id_curso);

-- 6. Lecciones
CREATE TABLE lecciones (
    id_leccion BIGSERIAL PRIMARY KEY,
    id_nivel BIGINT NOT NULL REFERENCES niveles(id_nivel) ON DELETE CASCADE,
    titulo VARCHAR(150) NOT NULL,
    orden INT NOT NULL,
    descripcion TEXT
);
CREATE INDEX idx_lecciones_id_nivel ON lecciones(id_nivel);

-- 7. Ejercicios
CREATE TABLE ejercicios (
    id_ejercicio BIGSERIAL PRIMARY KEY,
    id_leccion BIGINT NOT NULL REFERENCES lecciones(id_leccion) ON DELETE CASCADE,
    tipo_ejercicio VARCHAR(20) NOT NULL CHECK (tipo_ejercicio IN ('OPCION_MULTIPLE', 'ENTREVISTA')),
    enunciado TEXT NOT NULL,
    orden INT NOT NULL,
    respuesta_correcta TEXT,
    explicacion TEXT
);
CREATE INDEX idx_ejercicios_id_leccion ON ejercicios(id_leccion);

-- 8. Opciones de Ejercicio
CREATE TABLE opciones_ejercicio (
    id_opcion BIGSERIAL PRIMARY KEY,
    id_ejercicio BIGINT NOT NULL REFERENCES ejercicios(id_ejercicio) ON DELETE CASCADE,
    texto_opcion TEXT NOT NULL,
    es_correcta BOOLEAN NOT NULL DEFAULT FALSE
);
CREATE INDEX idx_opciones_ejercicio_id_ejercicio ON opciones_ejercicio(id_ejercicio);

-- 9. Progreso Leccion
CREATE TABLE progreso_leccion (
    id_progreso BIGSERIAL PRIMARY KEY,
    id_usuario BIGINT NOT NULL REFERENCES usuarios(id_usuario) ON DELETE CASCADE,
    id_leccion BIGINT NOT NULL REFERENCES lecciones(id_leccion) ON DELETE CASCADE,
    aciertos INT NOT NULL,
    total_preguntas INT NOT NULL,
    xp_ganado INT NOT NULL DEFAULT 0,
    fecha_completado TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    uuid_cliente UUID NOT NULL UNIQUE
);
CREATE INDEX idx_progreso_leccion_id_usuario ON progreso_leccion(id_usuario);
CREATE INDEX idx_progreso_leccion_id_leccion ON progreso_leccion(id_leccion);
