-- ============================================================
-- TECHMENTOR - PostgreSQL 
-- ============================================================

DROP DATABASE IF EXISTS techmentor;
CREATE DATABASE techmentor;

\c techmentor

-- ============================================================
-- FUNCIONES Y TRIGGERS GLOBALES
-- ============================================================

CREATE OR REPLACE FUNCTION actualizar_fecha()
RETURNS TRIGGER AS $$
BEGIN
    NEW.fecha_actualizacion = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- ============================================================
-- 1. SEGURIDAD Y USUARIOS
-- ============================================================

CREATE TABLE roles (
    id_rol BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE estados_usuario (
    id_estado_usuario BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(30) NOT NULL UNIQUE
);

CREATE TABLE usuarios (
    id_usuario BIGSERIAL PRIMARY KEY,
    id_rol BIGINT NOT NULL REFERENCES roles(id_rol),
    id_estado_usuario BIGINT NOT NULL REFERENCES estados_usuario(id_estado_usuario),
    nombres VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE, -- Agregado para recuperación de cuenta/notificaciones
    fecha_nacimiento DATE,
    
    avatar_url VARCHAR(500),
    biografia TEXT,
    telefono VARCHAR(30),
    pais VARCHAR(100),
    ciudad VARCHAR(100),
    
    xp_total INTEGER NOT NULL DEFAULT 0 CHECK (xp_total >= 0),
    monedas INTEGER NOT NULL DEFAULT 0 CHECK (monedas >= 0),
    nivel_usuario INTEGER NOT NULL DEFAULT 1 CHECK (nivel_usuario >= 1),
    
    racha_actual INTEGER NOT NULL DEFAULT 0 CHECK (racha_actual >= 0),
    racha_maxima INTEGER NOT NULL DEFAULT 0 CHECK (racha_maxima >= 0),
    fecha_inicio_racha DATE,
    ultima_actividad_racha DATE,
    
    fecha_registro TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP, -- TIMESTAMPTZ para zonas horarias
    fecha_actualizacion TIMESTAMPTZ,
    
    CONSTRAINT ck_racha CHECK (racha_actual <= racha_maxima) -- Integridad de rachas
);

CREATE INDEX idx_usuarios_rol ON usuarios(id_rol);
CREATE INDEX idx_usuarios_estado ON usuarios(id_estado_usuario);

CREATE TRIGGER trg_usuarios_upd
    BEFORE UPDATE ON usuarios
    FOR EACH ROW EXECUTE FUNCTION actualizar_fecha();


CREATE TABLE credenciales_usuario (
    id_credencial BIGSERIAL PRIMARY KEY,
    id_usuario BIGINT NOT NULL UNIQUE REFERENCES usuarios(id_usuario) ON DELETE CASCADE,
    username VARCHAR(100) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    ultimo_login TIMESTAMPTZ,
    fecha_creacion TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMPTZ
);

CREATE TRIGGER trg_credenciales_upd
    BEFORE UPDATE ON credenciales_usuario
    FOR EACH ROW EXECUTE FUNCTION actualizar_fecha();


CREATE TABLE historial_estados_usuario (
    id_historial BIGSERIAL PRIMARY KEY,
    id_usuario BIGINT NOT NULL REFERENCES usuarios(id_usuario) ON DELETE CASCADE,
    id_estado_usuario BIGINT NOT NULL REFERENCES estados_usuario(id_estado_usuario),
    motivo VARCHAR(255),
    fecha_cambio TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_historial_usuario ON historial_estados_usuario(id_usuario);
CREATE INDEX idx_historial_estado_usuario ON historial_estados_usuario(id_estado_usuario); -- Índice faltante agregado

-- ============================================================
-- 2. CATÁLOGO EDUCATIVO
-- ============================================================

CREATE TABLE categorias (
    id_categoria BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL UNIQUE,
    descripcion TEXT,
    icono VARCHAR(500),
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    fecha_creacion TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE cursos (
    id_curso BIGSERIAL PRIMARY KEY,
    id_categoria BIGINT NOT NULL REFERENCES categorias(id_categoria),
    nombre VARCHAR(150) NOT NULL,
    descripcion TEXT,
    icono VARCHAR(500),
    xp_requerido INTEGER NOT NULL DEFAULT 0 CHECK (xp_requerido >= 0),
    xp_recompensa INTEGER NOT NULL DEFAULT 0 CHECK (xp_recompensa >= 0),
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    fecha_creacion TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMPTZ,
    CONSTRAINT uq_curso_categoria_nombre UNIQUE (id_categoria, nombre)
);

CREATE TRIGGER trg_cursos_upd
    BEFORE UPDATE ON cursos
    FOR EACH ROW EXECUTE FUNCTION actualizar_fecha();

CREATE TABLE niveles (
    id_nivel BIGSERIAL PRIMARY KEY,
    id_curso BIGINT NOT NULL REFERENCES cursos(id_curso) ON DELETE CASCADE,
    nombre VARCHAR(100) NOT NULL,
    descripcion TEXT,
    orden INTEGER NOT NULL CHECK (orden > 0),
    CONSTRAINT uq_nivel_curso_orden UNIQUE (id_curso, orden),
    CONSTRAINT uq_nivel_curso_nombre UNIQUE (id_curso, nombre)
);

CREATE TABLE lecciones (
    id_leccion BIGSERIAL PRIMARY KEY,
    id_nivel BIGINT NOT NULL REFERENCES niveles(id_nivel) ON DELETE CASCADE,
    titulo VARCHAR(150) NOT NULL,
    descripcion TEXT,
    contenido TEXT,
    orden INTEGER NOT NULL CHECK (orden > 0),
    xp_recompensa INTEGER NOT NULL DEFAULT 0 CHECK (xp_recompensa >= 0),
    CONSTRAINT uq_leccion_nivel_orden UNIQUE (id_nivel, orden),
    CONSTRAINT uq_leccion_nivel_titulo UNIQUE (id_nivel, titulo)
);

CREATE TABLE ejercicios (
    id_ejercicio BIGSERIAL PRIMARY KEY,
    id_leccion BIGINT NOT NULL REFERENCES lecciones(id_leccion) ON DELETE CASCADE,
    tipo_ejercicio VARCHAR(50) NOT NULL,
    enunciado TEXT NOT NULL,
    orden INTEGER NOT NULL CHECK (orden > 0),
    respuesta_correcta TEXT, -- Utilizar SOLO para ejercicios de respuesta escrita/abierta
    explicacion TEXT,
    xp_recompensa INTEGER NOT NULL DEFAULT 0 CHECK (xp_recompensa >= 0),
    CONSTRAINT uq_ejercicio_leccion_orden UNIQUE (id_leccion, orden)
);

CREATE TABLE opciones_ejercicio (
    id_opcion BIGSERIAL PRIMARY KEY,
    id_ejercicio BIGINT NOT NULL REFERENCES ejercicios(id_ejercicio) ON DELETE CASCADE,
    texto TEXT NOT NULL,
    es_correcta BOOLEAN NOT NULL DEFAULT FALSE, -- Utilizar para ejercicios de opción múltiple
    orden INTEGER,
    CONSTRAINT uq_opcion_ejercicio_orden UNIQUE (id_ejercicio, orden)
);

-- ============================================================
-- 3. PROFESIONES E INTERESES
-- ============================================================

CREATE TABLE profesiones (
    id_profesion BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL UNIQUE,
    descripcion TEXT,
    activo BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE usuario_profesiones (
    id_usuario_profesion BIGSERIAL PRIMARY KEY,
    id_usuario BIGINT NOT NULL REFERENCES usuarios(id_usuario) ON DELETE CASCADE,
    id_profesion BIGINT NOT NULL REFERENCES profesiones(id_profesion),
    principal BOOLEAN NOT NULL DEFAULT FALSE,
    fecha_asignacion TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_usuario_profesion UNIQUE (id_usuario, id_profesion)
);

CREATE INDEX idx_usuario_profesiones_profesion ON usuario_profesiones(id_profesion);
-- Índice parcial para garantizar una única profesión principal por usuario
CREATE UNIQUE INDEX uq_usuario_profesion_principal ON usuario_profesiones(id_usuario) WHERE principal;

-- ============================================================
-- 4. DIAGNÓSTICO / NIVELACIÓN
-- ============================================================

CREATE TABLE categorias_pregunta (
    id_categoria_pregunta BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL UNIQUE,
    descripcion TEXT
);

CREATE TABLE examenes_diagnostico (
    id_examen BIGSERIAL PRIMARY KEY,
    id_curso BIGINT REFERENCES cursos(id_curso),
    nombre VARCHAR(150) NOT NULL,
    descripcion TEXT,
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT uq_examen_curso_nombre UNIQUE (id_curso, nombre)
);

-- Evitar nombres de examen duplicados cuando no están atados a un curso (id_curso es NULL)
CREATE UNIQUE INDEX uq_examen_global_nombre ON examenes_diagnostico(nombre) WHERE id_curso IS NULL;

CREATE TABLE preguntas_diagnostico (
    id_pregunta BIGSERIAL PRIMARY KEY,
    id_examen BIGINT NOT NULL REFERENCES examenes_diagnostico(id_examen) ON DELETE CASCADE,
    id_categoria_pregunta BIGINT NOT NULL REFERENCES categorias_pregunta(id_categoria_pregunta),
    enunciado TEXT NOT NULL,
    nivel_dificultad VARCHAR(30),
    tipo_pregunta VARCHAR(50) NOT NULL,
    puntaje INTEGER NOT NULL DEFAULT 1 CHECK (puntaje > 0),
    orden INTEGER NOT NULL CHECK (orden > 0),
    CONSTRAINT uq_pregunta_examen_orden UNIQUE (id_examen, orden)
);

CREATE INDEX idx_preguntas_categoria ON preguntas_diagnostico(id_categoria_pregunta);

CREATE TABLE opciones_diagnostico (
    id_opcion_diagnostico BIGSERIAL PRIMARY KEY,
    id_pregunta BIGINT NOT NULL REFERENCES preguntas_diagnostico(id_pregunta) ON DELETE CASCADE,
    texto TEXT NOT NULL,
    es_correcta BOOLEAN NOT NULL DEFAULT FALSE,
    orden INTEGER,
    CONSTRAINT uq_opcion_diagnostico_orden UNIQUE (id_pregunta, orden),
    -- Constraint necesario para la FK compuesta en respuestas_diagnostico
    CONSTRAINT uq_opcion_pregunta UNIQUE (id_opcion_diagnostico, id_pregunta) 
);

CREATE TABLE resultados_diagnostico (
    id_resultado BIGSERIAL PRIMARY KEY,
    id_usuario BIGINT NOT NULL REFERENCES usuarios(id_usuario) ON DELETE CASCADE,
    id_examen BIGINT NOT NULL REFERENCES examenes_diagnostico(id_examen),
    id_nivel_asignado BIGINT REFERENCES niveles(id_nivel),
    puntaje_obtenido INTEGER NOT NULL DEFAULT 0 CHECK (puntaje_obtenido >= 0),
    puntaje_maximo INTEGER NOT NULL DEFAULT 0 CHECK (puntaje_maximo >= 0),
    fecha_realizacion TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT ck_resultado_puntaje CHECK (puntaje_obtenido <= puntaje_maximo)
);

CREATE INDEX idx_resultados_usuario ON resultados_diagnostico(id_usuario);
CREATE INDEX idx_resultados_examen ON resultados_diagnostico(id_examen);
CREATE INDEX idx_resultados_nivel ON resultados_diagnostico(id_nivel_asignado); -- Índice faltante agregado

CREATE TABLE respuestas_diagnostico (
    id_respuesta BIGSERIAL PRIMARY KEY,
    id_resultado BIGINT NOT NULL REFERENCES resultados_diagnostico(id_resultado) ON DELETE CASCADE,
    id_pregunta BIGINT NOT NULL REFERENCES preguntas_diagnostico(id_pregunta),
    id_opcion_seleccionada BIGINT,
    CONSTRAINT uq_respuesta_resultado_pregunta UNIQUE (id_resultado, id_pregunta),
    -- FK Compuesta: Evita que el usuario seleccione una opción que pertenece a otra pregunta
    CONSTRAINT fk_respuesta_opcion_pregunta FOREIGN KEY (id_opcion_seleccionada, id_pregunta) 
        REFERENCES opciones_diagnostico(id_opcion_diagnostico, id_pregunta)
);

CREATE INDEX idx_respuestas_pregunta ON respuestas_diagnostico(id_pregunta);

-- ============================================================
-- 5. PROGRESO
-- ============================================================

CREATE TABLE progreso_curso (
    id_progreso BIGSERIAL PRIMARY KEY,
    id_usuario BIGINT NOT NULL REFERENCES usuarios(id_usuario) ON DELETE CASCADE,
    id_curso BIGINT NOT NULL REFERENCES cursos(id_curso),
    id_nivel_actual BIGINT REFERENCES niveles(id_nivel),
    fecha_inicio TIMESTAMPTZ, -- Nullable y sin default para que concuerde con 'NO_INICIADO'
    fecha_ultima_actividad TIMESTAMPTZ,
    estado VARCHAR(30) NOT NULL DEFAULT 'NO_INICIADO' 
        CHECK (estado IN ('NO_INICIADO', 'EN_CURSO', 'COMPLETADO', 'ABANDONADO')),
    xp_ganado INTEGER NOT NULL DEFAULT 0 CHECK (xp_ganado >= 0),
    fecha_completado TIMESTAMPTZ,
    CONSTRAINT uq_progreso_usuario_curso UNIQUE (id_usuario, id_curso)
);

CREATE INDEX idx_progreso_curso_curso ON progreso_curso(id_curso);
CREATE INDEX idx_progreso_curso_nivel ON progreso_curso(id_nivel_actual); -- Índice faltante agregado

CREATE TABLE progreso_leccion (
    id_progreso_leccion BIGSERIAL PRIMARY KEY,
    id_usuario BIGINT NOT NULL REFERENCES usuarios(id_usuario) ON DELETE CASCADE,
    id_leccion BIGINT NOT NULL REFERENCES lecciones(id_leccion),
    estado VARCHAR(30) NOT NULL DEFAULT 'NO_INICIADO'
        CHECK (estado IN ('NO_INICIADO', 'EN_CURSO', 'COMPLETADO')),
    puntaje INTEGER NOT NULL DEFAULT 0 CHECK (puntaje >= 0),
    intentos INTEGER NOT NULL DEFAULT 0 CHECK (intentos >= 0),
    fecha_inicio TIMESTAMPTZ,
    fecha_ultima_actividad TIMESTAMPTZ,
    fecha_completado TIMESTAMPTZ,
    xp_ganado INTEGER NOT NULL DEFAULT 0 CHECK (xp_ganado >= 0),
    CONSTRAINT uq_progreso_usuario_leccion UNIQUE (id_usuario, id_leccion)
);

CREATE INDEX idx_progreso_leccion_leccion ON progreso_leccion(id_leccion);

-- ============================================================
-- 6. GAMIFICACIÓN
-- ============================================================

CREATE TABLE misiones (
    id_mision BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(150) NOT NULL UNIQUE,
    descripcion TEXT,
    tipo VARCHAR(50) NOT NULL,
    meta INTEGER NOT NULL CHECK (meta > 0),
    xp_recompensa INTEGER NOT NULL DEFAULT 0 CHECK (xp_recompensa >= 0),
    fecha_inicio TIMESTAMPTZ,
    fecha_fin TIMESTAMPTZ,
    activa BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT ck_mision_fechas CHECK (fecha_fin IS NULL OR fecha_inicio IS NULL OR fecha_fin >= fecha_inicio) -- Integridad de fechas
);

CREATE TABLE misiones_usuario (
    id_mision_usuario BIGSERIAL PRIMARY KEY,
    id_usuario BIGINT NOT NULL REFERENCES usuarios(id_usuario) ON DELETE CASCADE,
    id_mision BIGINT NOT NULL REFERENCES misiones(id_mision),
    progreso_actual INTEGER NOT NULL DEFAULT 0 CHECK (progreso_actual >= 0),
    completada BOOLEAN NOT NULL DEFAULT FALSE,
    fecha_asignacion TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_completada TIMESTAMPTZ,
    CONSTRAINT uq_mision_usuario UNIQUE (id_usuario, id_mision)
);

CREATE INDEX idx_misiones_usuario_mision ON misiones_usuario(id_mision);

CREATE TABLE recompensas (
    id_recompensa BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(150) NOT NULL UNIQUE,
    descripcion TEXT,
    tipo VARCHAR(50) NOT NULL,
    cantidad INTEGER NOT NULL DEFAULT 0 CHECK (cantidad >= 0),
    costo_monedas INTEGER NOT NULL DEFAULT 0 CHECK (costo_monedas >= 0),
    dia_secuencia_requerido INTEGER CHECK (dia_secuencia_requerido > 0),
    activa BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE recompensas_usuario (
    id_recompensa_usuario BIGSERIAL PRIMARY KEY,
    id_usuario BIGINT NOT NULL REFERENCES usuarios(id_usuario) ON DELETE CASCADE,
    id_recompensa BIGINT NOT NULL REFERENCES recompensas(id_recompensa),
    fecha_obtencion TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    reclamada BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE INDEX idx_recompensas_usuario_recompensa ON recompensas_usuario(id_recompensa);
CREATE INDEX idx_recompensas_usuario_usuario ON recompensas_usuario(id_usuario); -- Índice faltante agregado

-- ============================================================
-- 7. DATOS INICIALES
-- ============================================================

INSERT INTO roles (nombre) VALUES
('ALUMNO'), ('ADMINISTRADOR'), ('MAESTRO'), ('USUARIO_VIP');

INSERT INTO estados_usuario (nombre) VALUES
('ACTIVO'), ('INACTIVO'), ('BLOQUEADO'), ('SUSPENDIDO');

INSERT INTO categorias (nombre, descripcion) VALUES
('Backend', 'Desarrollo de aplicaciones del lado servidor'),
('Frontend', 'Desarrollo de interfaces web'),
('Base de Datos', 'Diseño y administración de bases de datos'),
('Inglés', 'Aprendizaje del idioma inglés');

INSERT INTO recompensas
    (nombre, descripcion, tipo, cantidad, costo_monedas, dia_secuencia_requerido)
VALUES
    ('XP Básica', 'Recompensa de experiencia', 'XP', 50, 0, NULL),
    ('Monedas Básicas', 'Recompensa de monedas', 'MONEDAS', 25, 0, NULL),
    ('Recompensa Día 1', 'Recompensa de racha de 1 día', 'MONEDAS', 10, 0, 1),
    ('Recompensa Día 7', 'Recompensa de racha de 7 días', 'MONEDAS', 100, 0, 7);