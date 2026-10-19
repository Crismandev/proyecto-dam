-- =============================================================================
-- TechMentor — datos DEMO para el dashboard web (opcional)
-- Ejecutar DESPUÉS de techmentor.sql y datos_prueba.sql:
--   psql -U postgres -d techmentor -f datos_demo.sql
-- Es idempotente: se puede correr varias veces sin duplicar.
-- Solo crea estudiantes con correo @demo.techmentor.test (contraseña: Demo123*).
-- Para borrarlos:
--   DELETE FROM usuarios WHERE email LIKE '%@demo.techmentor.test';
-- =============================================================================

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

-- ---- Credenciales (username = parte local del correo con punto → guion bajo) ----
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
