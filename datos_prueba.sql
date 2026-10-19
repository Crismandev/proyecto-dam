-- ============================================================
-- TECHMENTOR - DATOS DE PRUEBA (solo desarrollo, NO es una migración Flyway)
-- Ejecutar DESPUÉS de que la app haya aplicado V1 y V2:
--     psql -U postgres -d techmentor -f datos_prueba.sql
-- Contraseñas de prueba:
--     admin / Admin123*     maestro / Maestro123*
--     alumno / Alumno123*   vip / Vip123*
-- Los links de video son MARCADORES: reemplaza REEMPLAZAR_ID por videos reales.
-- ============================================================

CREATE EXTENSION IF NOT EXISTS pgcrypto;  -- para generar hashes bcrypt compatibles con Spring

-- ---------- 1. Usuarios (uno por rol) y credenciales ----------
INSERT INTO usuarios (id_rol, id_estado_usuario, nombres, apellidos, email, email_verificado, fecha_verificacion_email)
SELECT r.id_rol,
       (SELECT id_estado_usuario FROM estados_usuario WHERE nombre = 'ACTIVO'),
       v.nombres, v.apellidos, v.email, TRUE, CURRENT_TIMESTAMP
FROM (VALUES
    ('ADMINISTRADOR', 'Admin',   'TechMentor', 'admin@techmentor.test'),
    ('MAESTRO',       'Maestro', 'TechMentor', 'maestro@techmentor.test'),
    ('ALUMNO',        'Alumno',  'TechMentor', 'alumno@techmentor.test'),
    ('USUARIO_VIP',   'Vip',     'TechMentor', 'vip@techmentor.test')
) AS v(rol, nombres, apellidos, email)
JOIN roles r ON r.nombre = v.rol;

INSERT INTO credenciales_usuario (id_usuario, username, password_hash)
SELECT u.id_usuario, v.username, crypt(v.clave, gen_salt('bf', 10))
FROM (VALUES
    ('admin@techmentor.test',   'admin',   'Admin123*'),
    ('maestro@techmentor.test', 'maestro', 'Maestro123*'),
    ('alumno@techmentor.test',  'alumno',  'Alumno123*'),
    ('vip@techmentor.test',     'vip',     'Vip123*')
) AS v(email, username, clave)
JOIN usuarios u ON u.email = v.email;

INSERT INTO historial_estados_usuario (id_usuario, id_estado_usuario, motivo)
SELECT id_usuario, id_estado_usuario, 'Alta de datos de prueba' FROM usuarios;

-- ---------- 2. Profesiones (= puestos a los que se postula) ----------
INSERT INTO profesiones (nombre, descripcion) VALUES
('Desarrollador Backend',  'Desarrollo de APIs y lógica de servidor'),
('Desarrollador Frontend', 'Desarrollo de interfaces web'),
('Analista de Datos',      'Consulta y análisis de información');

INSERT INTO usuario_profesiones (id_usuario, id_profesion, principal)
SELECT u.id_usuario, p.id_profesion, TRUE
FROM (VALUES ('alumno@techmentor.test', 'Desarrollador Backend'),
             ('vip@techmentor.test',    'Desarrollador Frontend')) AS v(email, profesion)
JOIN usuarios u ON u.email = v.email
JOIN profesiones p ON p.nombre = v.profesion;

-- ---------- 3. Cursos ----------
INSERT INTO cursos (id_categoria, nombre, descripcion, xp_recompensa)
SELECT c.id_categoria, v.curso, v.descripcion, v.xp
FROM (VALUES
    ('Backend',       'Java desde cero',     'Fundamentos del lenguaje Java',        100),
    ('Backend',       'Spring Boot básico',  'APIs REST con Spring Boot',            150),
    ('Base de Datos', 'SQL y PostgreSQL',    'Consultas y diseño de tablas',         120),
    ('Frontend',      'Angular inicial',     'Primeros pasos con Angular',           120),
    ('Inglés',        'Inglés técnico',      'Vocabulario para entrevistas de TI',    80)
) AS v(categoria, curso, descripcion, xp)
JOIN categorias c ON c.nombre = v.categoria;

-- Niveles: Java con 4, los demás con 3
INSERT INTO niveles (id_curso, nombre, orden)
SELECT c.id_curso, n.nombre, n.orden
FROM cursos c
JOIN (VALUES ('Inicial', 1), ('Básico', 2), ('Intermedio', 3), ('Avanzado', 4)) AS n(nombre, orden)
  ON (c.nombre = 'Java desde cero') OR (n.orden <= 3);

-- ---------- 4. Lecciones (2 por nivel inicial de cada curso) ----------
INSERT INTO lecciones (id_nivel, titulo, descripcion, contenido, orden, xp_recompensa)
SELECT n.id_nivel, v.titulo, v.descripcion, v.contenido, v.orden, 20
FROM niveles n
JOIN (VALUES
    (1, 'Introducción',   'Qué vas a aprender',        'Texto de bienvenida y objetivos del curso.'),
    (2, 'Primeros pasos', 'Conceptos básicos del tema', 'Explicación breve de los conceptos clave.')
) AS v(orden, titulo, descripcion, contenido) ON TRUE
WHERE n.orden = 1;

-- ---------- 5. Recursos por lección: 1 video + 1 texto ----------
INSERT INTO recursos_leccion (id_leccion, tipo, titulo, url, contenido, plataforma, duracion_segundos, orden)
SELECT l.id_leccion, 'VIDEO', 'Video: ' || l.titulo,
       'https://www.youtube.com/watch?v=REEMPLAZAR_ID', NULL, 'YOUTUBE', 300, 1
FROM lecciones l;

INSERT INTO recursos_leccion (id_leccion, tipo, titulo, url, contenido, plataforma, duracion_segundos, orden)
SELECT l.id_leccion, 'TEXTO', 'Resumen: ' || l.titulo, NULL,
       'Resumen escrito de la lección con los puntos principales.', NULL, NULL, 2
FROM lecciones l;

-- ---------- 6. Ejercicios y opciones (1 por lección) ----------
INSERT INTO ejercicios (id_leccion, tipo_ejercicio, enunciado, orden, explicacion, xp_recompensa)
SELECT l.id_leccion, 'OPCION_MULTIPLE', '¿Qué aprendiste en "' || l.titulo || '"?', 1,
       'La opción correcta resume el objetivo de la lección.', 10
FROM lecciones l;

INSERT INTO opciones_ejercicio (id_ejercicio, texto, es_correcta, orden)
SELECT e.id_ejercicio, o.texto, o.ok, o.orden
FROM ejercicios e
JOIN (VALUES ('Opción correcta', TRUE, 1), ('Opción incorrecta A', FALSE, 2), ('Opción incorrecta B', FALSE, 3))
     AS o(texto, ok, orden) ON TRUE;

-- ---------- 7. Diagnóstico adaptado por puesto ----------
INSERT INTO categorias_pregunta (nombre, descripcion) VALUES
('Fundamentos',    'Conceptos base de programación'),
('Lógica',         'Razonamiento y control de flujo'),
('Bases de datos', 'Consultas y modelado'),
('Interfaces web', 'HTML, CSS y frameworks de frontend');

INSERT INTO examenes_diagnostico (id_profesion, nombre, descripcion)
SELECT p.id_profesion, 'Diagnóstico ' || p.nombre, 'Examen de nivelación para el puesto ' || p.nombre
FROM profesiones p WHERE p.nombre IN ('Desarrollador Backend', 'Desarrollador Frontend');

-- Backend: 6 preguntas
INSERT INTO preguntas_diagnostico (id_examen, id_categoria_pregunta, enunciado, nivel_dificultad, tipo_pregunta, puntaje, orden)
SELECT e.id_examen, cp.id_categoria_pregunta, q.enunciado, q.dificultad, 'OPCION_MULTIPLE', 1, q.orden
FROM examenes_diagnostico e
JOIN (VALUES
    ('Fundamentos',    '¿Qué palabra clave declara una constante en Java?',            'BASICO',     1),
    ('Fundamentos',    '¿Qué tipo de dato guarda enteros de 32 bits en Java?',         'BASICO',     2),
    ('Lógica',         '¿Qué estructura repite código mientras se cumpla una condición?','BASICO',    3),
    ('Lógica',         '¿Cuántas veces corre: for (int i = 0; i < 3; i++)?',            'INTERMEDIO', 4),
    ('Bases de datos', '¿Qué sentencia SQL obtiene filas de una tabla?',                'BASICO',     5),
    ('Bases de datos', '¿Qué cláusula filtra filas en una consulta SQL?',               'BASICO',     6)
) AS q(categoria, enunciado, dificultad, orden) ON TRUE
JOIN categorias_pregunta cp ON cp.nombre = q.categoria
WHERE e.nombre = 'Diagnóstico Desarrollador Backend';

-- Frontend: 3 preguntas
INSERT INTO preguntas_diagnostico (id_examen, id_categoria_pregunta, enunciado, nivel_dificultad, tipo_pregunta, puntaje, orden)
SELECT e.id_examen, cp.id_categoria_pregunta, q.enunciado, q.dificultad, 'OPCION_MULTIPLE', 1, q.orden
FROM examenes_diagnostico e
JOIN (VALUES
    ('Interfaces web', '¿Qué etiqueta HTML define un enlace?',                   'BASICO', 1),
    ('Interfaces web', '¿Qué lenguaje da estilo a una página web?',               'BASICO', 2),
    ('Lógica',         '¿Qué estructura permite decidir entre dos caminos?',      'BASICO', 3)
) AS q(categoria, enunciado, dificultad, orden) ON TRUE
JOIN categorias_pregunta cp ON cp.nombre = q.categoria
WHERE e.nombre = 'Diagnóstico Desarrollador Frontend';

-- Opciones: la 1 de cada pregunta es la correcta
INSERT INTO opciones_diagnostico (id_pregunta, texto, es_correcta, orden)
SELECT p.id_pregunta, o.texto, o.orden = 1, o.orden
FROM preguntas_diagnostico p
JOIN (VALUES (1, 'Correcta'), (2, 'Distractor A'), (3, 'Distractor B')) AS o(orden, texto) ON TRUE;

-- ---------- 8. Reglas de recomendación ----------
-- Qué cursos exige cada puesto
INSERT INTO profesion_cursos (id_profesion, id_curso, prioridad)
SELECT p.id_profesion, c.id_curso, v.prioridad
FROM (VALUES
    ('Desarrollador Backend',  'Java desde cero',    1),
    ('Desarrollador Backend',  'Spring Boot básico', 2),
    ('Desarrollador Backend',  'SQL y PostgreSQL',   2),
    ('Desarrollador Frontend', 'Angular inicial',    1),
    ('Desarrollador Frontend', 'Inglés técnico',     3),
    ('Analista de Datos',      'SQL y PostgreSQL',   1)
) AS v(profesion, curso, prioridad)
JOIN profesiones p ON p.nombre = v.profesion
JOIN cursos c ON c.nombre = v.curso;

-- Qué curso refuerza cada categoría de pregunta (umbral = % mínimo esperado)
INSERT INTO categoria_pregunta_cursos (id_categoria_pregunta, id_curso, umbral_porcentaje)
SELECT cp.id_categoria_pregunta, c.id_curso, v.umbral
FROM (VALUES
    ('Fundamentos',    'Java desde cero',  60),
    ('Lógica',         'Java desde cero',  50),
    ('Bases de datos', 'SQL y PostgreSQL', 60),
    ('Interfaces web', 'Angular inicial',  60)
) AS v(categoria, curso, umbral)
JOIN categorias_pregunta cp ON cp.nombre = v.categoria
JOIN cursos c ON c.nombre = v.curso;

-- ---------- 9. Gamificación ----------
INSERT INTO misiones (nombre, descripcion, tipo, meta, xp_recompensa) VALUES
('Primera lección',  'Completa tu primera lección', 'LECCIONES', 1,   50),
('Racha de 3 días',  'Estudia 3 días seguidos',     'RACHA',     3,   100),
('Gana 200 XP',      'Acumula 200 puntos de XP',    'XP',        200, 75);

-- ============================================================
-- Complemento: perfil, progreso y misiones del Alumno
-- (bloque actualizado en Descargas/datos_prueba.sql)
-- ============================================================

-- ---------- 10. Actualización de perfil del Alumno de prueba ----------
UPDATE usuarios
SET xp_total = 240,
    monedas = 50,
    racha_actual = 7,
    racha_maxima = 14,
    biografia = 'Estudiante apasionado de desarrollo backend y bases de datos.',
    telefono = '+51 987 654 321',
    pais = 'Perú',
    ciudad = 'Lima'
WHERE email = 'alumno@techmentor.test';

-- ---------- 11. Misiones adicionales y asignación al alumno ----------
INSERT INTO misiones (nombre, descripcion, tipo, meta, xp_recompensa, activa) VALUES
('Completa 2 lecciones', 'Avanza en tus cursos completando 2 lecciones hoy', 'DIARIA', 2, 50, TRUE),
('Gana 100 XP esta semana', 'Participa activamente en tus cursos y acumula experiencia', 'SEMANAL', 100, 120, TRUE),
('Primer Examen de Diagnóstico', 'Mide tus habilidades técnicas iniciales', 'ESPECIAL', 1, 150, TRUE)
ON CONFLICT (nombre) DO NOTHING;

INSERT INTO misiones_usuario (id_usuario, id_mision, progreso_actual, completada, fecha_asignacion)
SELECT u.id_usuario, m.id_mision, v.progreso, v.completada, CURRENT_TIMESTAMP
FROM (VALUES
    ('Completa 2 lecciones', 1, FALSE),
    ('Gana 100 XP esta semana', 60, FALSE),
    ('Primer Examen de Diagnóstico', 1, TRUE)
) AS v(nombre_mision, progreso, completada)
JOIN misiones m ON m.nombre = v.nombre_mision
CROSS JOIN (SELECT id_usuario FROM usuarios WHERE email = 'alumno@techmentor.test') u
ON CONFLICT (id_usuario, id_mision) DO NOTHING;

-- ---------- 12. Progreso de Cursos para el Alumno ----------
INSERT INTO progreso_curso (id_usuario, id_curso, id_nivel_actual, fecha_inicio, fecha_ultima_actividad, estado, xp_ganado)
SELECT u.id_usuario, c.id_curso,
       (SELECT id_nivel FROM niveles WHERE id_curso = c.id_curso ORDER BY orden ASC LIMIT 1),
       CURRENT_TIMESTAMP - INTERVAL '3 days',
       CURRENT_TIMESTAMP,
       'EN_CURSO',
       40
FROM cursos c
CROSS JOIN (SELECT id_usuario FROM usuarios WHERE email = 'alumno@techmentor.test') u
WHERE c.nombre = 'Java desde cero'
ON CONFLICT (id_usuario, id_curso) DO UPDATE
SET estado = EXCLUDED.estado, xp_ganado = EXCLUDED.xp_ganado;

INSERT INTO progreso_curso (id_usuario, id_curso, id_nivel_actual, fecha_inicio, fecha_ultima_actividad, fecha_completado, estado, xp_ganado)
SELECT u.id_usuario, c.id_curso,
       (SELECT id_nivel FROM niveles WHERE id_curso = c.id_curso ORDER BY orden DESC LIMIT 1),
       CURRENT_TIMESTAMP - INTERVAL '10 days',
       CURRENT_TIMESTAMP - INTERVAL '1 day',
       CURRENT_TIMESTAMP - INTERVAL '1 day',
       'COMPLETADO',
       120
FROM cursos c
CROSS JOIN (SELECT id_usuario FROM usuarios WHERE email = 'alumno@techmentor.test') u
WHERE c.nombre = 'SQL y PostgreSQL'
ON CONFLICT (id_usuario, id_curso) DO UPDATE
SET estado = EXCLUDED.estado, xp_ganado = EXCLUDED.xp_ganado;

-- ---------- 13. Progreso de Lecciones para el Alumno ----------
INSERT INTO progreso_leccion (id_usuario, id_leccion, estado, puntaje, intentos, fecha_inicio, fecha_completado, xp_ganado)
SELECT u.id_usuario, l.id_leccion, 'COMPLETADO', 20, 1,
       CURRENT_TIMESTAMP - INTERVAL '2 days', CURRENT_TIMESTAMP - INTERVAL '2 days', 20
FROM lecciones l
JOIN niveles n ON n.id_nivel = l.id_nivel
JOIN cursos c ON c.id_curso = n.id_curso
CROSS JOIN (SELECT id_usuario FROM usuarios WHERE email = 'alumno@techmentor.test') u
WHERE c.nombre = 'Java desde cero' AND l.orden = 1
ON CONFLICT (id_usuario, id_leccion) DO NOTHING;

-- ---------- 14. Recompensas del Alumno ----------
INSERT INTO recompensas_usuario (id_usuario, id_recompensa, reclamada)
SELECT u.id_usuario, r.id_recompensa, TRUE
FROM recompensas r
CROSS JOIN (SELECT id_usuario FROM usuarios WHERE email = 'alumno@techmentor.test') u
WHERE r.nombre IN ('XP Básica', 'Recompensa Día 1')
ON CONFLICT DO NOTHING;
