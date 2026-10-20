-- ============================================================
-- TECHMENTOR - DATOS DE PRUEBA + DEMO (solo desarrollo, NO es una migración Flyway)
-- Ejecutar DESPUÉS de techmentor.sql, sobre una base recién creada:
--     psql -U postgres -d techmentor -f techmentor.sql
--     psql -U postgres -d techmentor -f datos.sql
-- Parte 1 (secciones 1-14): catálogo, diagnóstico, misiones y cuentas por rol.
--     admin / Admin123*     maestro / Maestro123*
--     alumno / Alumno123*   vip / Vip123*
-- Parte 2 (sección 15): 24 estudiantes demo para el dashboard web,
--     correos @demo.techmentor.test, contraseña Demo123*.
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

-- ============================================================
-- 15. DATOS DEMO para el dashboard web
-- Idempotente: solo toca correos @demo.techmentor.test.
-- Username = parte local del correo con punto → guion bajo (ej. lucia_ramirez).
-- Para borrarlos:
--   DELETE FROM usuarios WHERE email LIKE '%@demo.techmentor.test';
-- ============================================================

BEGIN;

-- ---- Estudiantes demo (registros repartidos en los últimos 12 meses) ----
INSERT INTO usuarios (id_rol, id_estado_usuario, nombres, apellidos, email, pais, ciudad,
                      xp_total, monedas, nivel_usuario, racha_actual, racha_maxima,
                      email_verificado, fecha_verificacion_email, fecha_registro)
SELECT r.id_rol,
       e.id_estado_usuario,
       v.nombres, v.apellidos, v.email, 'Perú', v.ciudad,
       v.xp, v.xp / 10, GREATEST(1, v.xp / 250 + 1), v.racha, GREATEST(v.racha, v.racha_max),
       TRUE, CURRENT_TIMESTAMP - make_interval(days => v.dias),
       CURRENT_TIMESTAMP - make_interval(days => v.dias)
FROM (VALUES
    ('ALUMNO',      'ACTIVO',   'Lucía',     'Ramírez Torres',   'lucia.ramirez@demo.techmentor.test',     'Lima',      1240, 12, 21, 340),
    ('ALUMNO',      'ACTIVO',   'Diego',     'Fernández Rojas',  'diego.fernandez@demo.techmentor.test',   'Arequipa',   860,  5, 14, 320),
    ('USUARIO_VIP', 'ACTIVO',   'Valeria',   'Quispe Mamani',    'valeria.quispe@demo.techmentor.test',    'Cusco',     1980, 30, 30, 300),
    ('ALUMNO',      'ACTIVO',   'Mateo',     'Gutiérrez Silva',  'mateo.gutierrez@demo.techmentor.test',   'Lima',       420,  2,  9, 285),
    ('ALUMNO',      'INACTIVO', 'Camila',    'Vargas León',      'camila.vargas@demo.techmentor.test',     'Trujillo',   150,  0,  4, 270),
    ('ALUMNO',      'ACTIVO',   'Sebastián', 'Castillo Paredes', 'sebastian.castillo@demo.techmentor.test','Piura',      980,  8, 15, 240),
    ('USUARIO_VIP', 'ACTIVO',   'Daniela',   'Chávez Huamán',    'daniela.chavez@demo.techmentor.test',    'Lima',      1560, 18, 25, 225),
    ('ALUMNO',      'ACTIVO',   'Nicolás',   'Flores Medina',    'nicolas.flores@demo.techmentor.test',    'Chiclayo',   610,  3, 10, 210),
    ('ALUMNO',      'ACTIVO',   'Sofía',     'Rojas Cárdenas',   'sofia.rojas@demo.techmentor.test',       'Lima',       730,  6, 12, 190),
    ('ALUMNO',      'BLOQUEADO','Joaquín',   'Herrera Salazar',  'joaquin.herrera@demo.techmentor.test',   'Huancayo',    90,  0,  2, 175),
    ('ALUMNO',      'ACTIVO',   'Isabella',  'Mendoza Ríos',     'isabella.mendoza@demo.techmentor.test',  'Arequipa',  1110, 10, 18, 160),
    ('USUARIO_VIP', 'ACTIVO',   'Gabriel',   'Sánchez Vera',     'gabriel.sanchez@demo.techmentor.test',   'Lima',      2240, 41, 41, 150),
    ('ALUMNO',      'ACTIVO',   'Antonella', 'Díaz Campos',      'antonella.diaz@demo.techmentor.test',    'Ica',        380,  1,  7, 130),
    ('ALUMNO',      'ACTIVO',   'Thiago',    'Morales Espinoza', 'thiago.morales@demo.techmentor.test',    'Lima',       540,  4,  8, 115),
    ('ALUMNO',      'ACTIVO',   'Renata',    'Ortiz Calderón',   'renata.ortiz@demo.techmentor.test',      'Cusco',      470,  0,  6, 100),
    ('ALUMNO',      'ACTIVO',   'Emiliano',  'Ruiz Palomino',    'emiliano.ruiz@demo.techmentor.test',     'Tacna',      320,  2,  5,  85),
    ('USUARIO_VIP', 'ACTIVO',   'Martina',   'Navarro Aguilar',  'martina.navarro@demo.techmentor.test',   'Lima',       890,  9, 11,  70),
    ('ALUMNO',      'ACTIVO',   'Benjamín',  'Ponce Villanueva', 'benjamin.ponce@demo.techmentor.test',    'Piura',      260,  1,  3,  55),
    ('ALUMNO',      'ACTIVO',   'Mía',       'Salinas Romero',   'mia.salinas@demo.techmentor.test',       'Lima',       210,  3,  3,  40),
    ('ALUMNO',      'ACTIVO',   'Santiago',  'Delgado Cruz',     'santiago.delgado@demo.techmentor.test',  'Arequipa',   180,  2,  2,  28),
    ('ALUMNO',      'ACTIVO',   'Valentina', 'Reyes Cáceres',    'valentina.reyes@demo.techmentor.test',   'Trujillo',   120,  1,  1,  18),
    ('USUARIO_VIP', 'ACTIVO',   'Adrián',    'Guerrero Lozano',  'adrian.guerrero@demo.techmentor.test',   'Lima',       340,  5,  5,  12),
    ('ALUMNO',      'ACTIVO',   'Ximena',    'Paredes Soto',     'ximena.paredes@demo.techmentor.test',    'Chiclayo',    60,  1,  1,   6),
    ('ALUMNO',      'ACTIVO',   'Leonardo',  'Cabrera Núñez',    'leonardo.cabrera@demo.techmentor.test',  'Lima',        30,  1,  1,   2)
) AS v(rol, estado, nombres, apellidos, email, ciudad, xp, racha, racha_max, dias)
JOIN roles r ON r.nombre = v.rol
JOIN estados_usuario e ON e.nombre = v.estado
ON CONFLICT (email) DO NOTHING;

-- ---- Credenciales demo ----
INSERT INTO credenciales_usuario (id_usuario, username, password_hash)
SELECT u.id_usuario,
       replace(split_part(u.email, '@', 1), '.', '_'),
       crypt('Demo123*', gen_salt('bf', 10))
FROM usuarios u
WHERE u.email LIKE '%@demo.techmentor.test'
ON CONFLICT DO NOTHING;

-- ---- Inscripciones a cursos con estados variados (determinista) ----
INSERT INTO progreso_curso (id_usuario, id_curso, id_nivel_actual, fecha_inicio, fecha_ultima_actividad,
                            fecha_completado, estado, xp_ganado)
SELECT u.id_usuario,
       c.id_curso,
       (SELECT n.id_nivel FROM niveles n WHERE n.id_curso = c.id_curso ORDER BY n.orden ASC LIMIT 1),
       inicio,
       actividad,
       CASE WHEN estado = 'COMPLETADO' THEN actividad END,
       estado,
       CASE estado WHEN 'COMPLETADO' THEN c.xp_recompensa
                   WHEN 'EN_CURSO'   THEN c.xp_recompensa / 2
                   ELSE 0 END
FROM usuarios u
CROSS JOIN cursos c
CROSS JOIN LATERAL (
    SELECT CASE (u.id_usuario + c.id_curso) % 5
               WHEN 0 THEN 'COMPLETADO'
               WHEN 1 THEN 'COMPLETADO'
               WHEN 4 THEN 'ABANDONADO'
               ELSE 'EN_CURSO' END AS estado,
           u.fecha_registro + make_interval(days => 1 + (c.id_curso::int % 3)) AS inicio,
           LEAST(CURRENT_TIMESTAMP,
                 u.fecha_registro + make_interval(days => 3 + ((u.id_usuario * 7 + c.id_curso * 11) % 60)::int)) AS actividad
) s
WHERE u.email LIKE '%@demo.techmentor.test'
  AND (u.id_usuario * 3 + c.id_curso) % 7 NOT IN (0, 3)
ON CONFLICT (id_usuario, id_curso) DO NOTHING;

-- ---- Lecciones completadas de los cursos terminados ----
INSERT INTO progreso_leccion (id_usuario, id_leccion, estado, puntaje, intentos, fecha_inicio, fecha_completado, xp_ganado)
SELECT pc.id_usuario, l.id_leccion, 'COMPLETADO', 80 + (l.id_leccion % 3) * 10, 1 + (l.id_leccion % 2),
       pc.fecha_inicio, pc.fecha_ultima_actividad, l.xp_recompensa
FROM progreso_curso pc
JOIN usuarios u ON u.id_usuario = pc.id_usuario AND u.email LIKE '%@demo.techmentor.test'
JOIN niveles n ON n.id_curso = pc.id_curso
JOIN lecciones l ON l.id_nivel = n.id_nivel
WHERE pc.estado = 'COMPLETADO'
ON CONFLICT (id_usuario, id_leccion) DO NOTHING;

COMMIT;
