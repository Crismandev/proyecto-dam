-- ==========================================
-- TechMentor Seed Data (02_data.sql)
-- ==========================================

CREATE EXTENSION IF NOT EXISTS pgcrypto;

-- 1. Roles
INSERT INTO roles (nombre_rol, descripcion) VALUES
('ADMIN', 'Administrador del sistema con acceso total al portal'),
('ESTUDIANTE', 'Estudiante de la plataforma móvil');

-- 2. Usuarios
-- Passwords: Admin123*, Luis12345*, Ana12345*, Juan12345*, Carlos123*
INSERT INTO usuarios (id_rol, nombres, apellidos, correo, contrasena, fecha_nacimiento, fecha_registro, avatar, xp_total, monedas) VALUES
((SELECT id_rol FROM roles WHERE nombre_rol = 'ADMIN'), 'Administrador', 'Sistema', 'admin@techmentor.pe', crypt('Admin123*', gen_salt('bf')), '1990-01-01', CURRENT_TIMESTAMP - INTERVAL '30 days', 'avatar_admin.png', 0, 0),
((SELECT id_rol FROM roles WHERE nombre_rol = 'ESTUDIANTE'), 'Luis', 'Gómez', 'luis@correo.com', crypt('Luis12345*', gen_salt('bf')), '1998-05-12', CURRENT_TIMESTAMP - INTERVAL '10 days', 'avatar_luis.png', 240, 50),
((SELECT id_rol FROM roles WHERE nombre_rol = 'ESTUDIANTE'), 'Ana', 'Torres', 'ana@correo.com', crypt('Ana12345*', gen_salt('bf')), '2000-08-20', CURRENT_TIMESTAMP - INTERVAL '5 days', 'avatar_ana.png', 90, 20),
((SELECT id_rol FROM roles WHERE nombre_rol = 'ESTUDIANTE'), 'Juan', 'Pérez', 'juan@correo.com', crypt('Juan12345*', gen_salt('bf')), '1995-11-03', CURRENT_TIMESTAMP - INTERVAL '3 days', 'avatar_juan.png', 60, 10),
((SELECT id_rol FROM roles WHERE nombre_rol = 'ESTUDIANTE'), 'Carlos', 'Mendoza', 'carlos@correo.com', crypt('Carlos123*', gen_salt('bf')), '1997-02-14', CURRENT_TIMESTAMP - INTERVAL '1 days', 'avatar_carlos.png', 0, 0);

-- 3. Categorias
INSERT INTO categorias (nombre_categoria, descripcion, icono) VALUES
('Desarrollo', 'Cursos de programación web, móvil y backend', 'code'),
('Base de datos', 'Cursos de diseño, gestión y consultas SQL', 'database');

-- 4. Cursos
INSERT INTO cursos (id_categoria, nombre_curso, descripcion, icono, dificultad, xp_requerido, xp_recompensa) VALUES
((SELECT id_categoria FROM categorias WHERE nombre_categoria = 'Desarrollo'), 'APIs con Spring Boot', 'Aprende a construir servicios REST profesionales con Java y Spring Boot.', 'code', 'INTERMEDIO', 100, 100),
((SELECT id_categoria FROM categorias WHERE nombre_categoria = 'Desarrollo'), 'Desarrollo web con React', 'Construye interfaces web modernas y reactivas con React JS.', 'react', 'INTERMEDIO', 100, 100),
((SELECT id_categoria FROM categorias WHERE nombre_categoria = 'Base de datos'), 'Bases de datos con MySQL', 'Domina el diseño relacional y consultas SQL eficientes.', 'database', 'INTERMEDIO', 100, 100),
((SELECT id_categoria FROM categorias WHERE nombre_categoria = 'Desarrollo'), 'Programación orientada a objetos', 'Fundamentos de POO, clases, herencia y polimorfismo.', 'object', 'PRINCIPIANTE', 0, 100),
((SELECT id_categoria FROM categorias WHERE nombre_categoria = 'Desarrollo'), 'Desarrollo móvil con Android', 'Crea aplicaciones nativas en Android con Kotlin y Java.', 'android', 'PRINCIPIANTE', 0, 100);

-- 5. Niveles (Unidad 1 para cada curso)
INSERT INTO niveles (id_curso, nombre_nivel, orden, descripcion) VALUES
((SELECT id_curso FROM cursos WHERE nombre_curso = 'APIs con Spring Boot'), 'Unidad 1', 1, 'Fundamentos de servicios web REST'),
((SELECT id_curso FROM cursos WHERE nombre_curso = 'Desarrollo web con React'), 'Unidad 1', 1, 'Introducción a componentes y estado'),
((SELECT id_curso FROM cursos WHERE nombre_curso = 'Bases de datos con MySQL'), 'Unidad 1', 1, 'Conceptos básicos de SQL'),
((SELECT id_curso FROM cursos WHERE nombre_curso = 'Programación orientada a objetos'), 'Unidad 1', 1, 'Fundamentos de clases u objetos'),
((SELECT id_curso FROM cursos WHERE nombre_curso = 'Desarrollo móvil con Android'), 'Unidad 1', 1, 'Introducción a Android Studio');

-- 6. Lecciones
-- APIs con Spring Boot (Nivel 1)
INSERT INTO lecciones (id_nivel, titulo, orden, descripcion) VALUES
((SELECT id_nivel FROM niveles WHERE nombre_nivel = 'Unidad 1' AND id_curso = (SELECT id_curso FROM cursos WHERE nombre_curso = 'APIs con Spring Boot')), 'Primeros pasos', 1, 'Introducción al curso'),
((SELECT id_nivel FROM niveles WHERE nombre_nivel = 'Unidad 1' AND id_curso = (SELECT id_curso FROM cursos WHERE nombre_curso = 'APIs con Spring Boot')), 'Fundamentos de APIs', 2, 'Peticiones, respuestas y APIs'),
((SELECT id_nivel FROM niveles WHERE nombre_nivel = 'Unidad 1' AND id_curso = (SELECT id_curso FROM cursos WHERE nombre_curso = 'APIs con Spring Boot')), 'Tu primer reto práctico', 3, 'Práctica de entrevista');

-- React (Nivel 2)
INSERT INTO lecciones (id_nivel, titulo, orden, descripcion) VALUES
((SELECT id_nivel FROM niveles WHERE nombre_nivel = 'Unidad 1' AND id_curso = (SELECT id_curso FROM cursos WHERE nombre_curso = 'Desarrollo web con React')), 'Primeros pasos', 1, 'Introducción'),
((SELECT id_nivel FROM niveles WHERE nombre_nivel = 'Unidad 1' AND id_curso = (SELECT id_curso FROM cursos WHERE nombre_curso = 'Desarrollo web con React')), 'Componentes y props', 2, 'Componentes y propiedades'),
((SELECT id_nivel FROM niveles WHERE nombre_nivel = 'Unidad 1' AND id_curso = (SELECT id_curso FROM cursos WHERE nombre_curso = 'Desarrollo web con React')), 'Tu primer reto práctico', 3, 'Práctica');

-- MySQL (Nivel 3)
INSERT INTO lecciones (id_nivel, titulo, orden, descripcion) VALUES
((SELECT id_nivel FROM niveles WHERE nombre_nivel = 'Unidad 1' AND id_curso = (SELECT id_curso FROM cursos WHERE nombre_curso = 'Bases de datos con MySQL')), 'Primeros pasos', 1, 'Introducción'),
((SELECT id_nivel FROM niveles WHERE nombre_nivel = 'Unidad 1' AND id_curso = (SELECT id_curso FROM cursos WHERE nombre_curso = 'Bases de datos con MySQL')), 'Tu primera consulta', 2, 'Tablas, consultas y relaciones'),
((SELECT id_nivel FROM niveles WHERE nombre_nivel = 'Unidad 1' AND id_curso = (SELECT id_curso FROM cursos WHERE nombre_curso = 'Bases de datos con MySQL')), 'Tu primer reto práctico', 3, 'Práctica');

-- POO (Nivel 4)
INSERT INTO lecciones (id_nivel, titulo, orden, descripcion) VALUES
((SELECT id_nivel FROM niveles WHERE nombre_nivel = 'Unidad 1' AND id_curso = (SELECT id_curso FROM cursos WHERE nombre_curso = 'Programación orientada a objetos')), 'Primeros pasos', 1, 'Introducción'),
((SELECT id_nivel FROM niveles WHERE nombre_nivel = 'Unidad 1' AND id_curso = (SELECT id_curso FROM cursos WHERE nombre_curso = 'Programación orientada a objetos')), 'Clases y objetos', 2, 'Clases, objetos y comportamiento'),
((SELECT id_nivel FROM niveles WHERE nombre_nivel = 'Unidad 1' AND id_curso = (SELECT id_curso FROM cursos WHERE nombre_curso = 'Programación orientada a objetos')), 'Tu primer reto práctico', 3, 'Práctica');

-- Android (Nivel 5)
INSERT INTO lecciones (id_nivel, titulo, orden, descripcion) VALUES
((SELECT id_nivel FROM niveles WHERE nombre_nivel = 'Unidad 1' AND id_curso = (SELECT id_curso FROM cursos WHERE nombre_curso = 'Desarrollo móvil con Android')), 'Primeros pasos', 1, 'Introducción'),
((SELECT id_nivel FROM niveles WHERE nombre_nivel = 'Unidad 1' AND id_curso = (SELECT id_curso FROM cursos WHERE nombre_curso = 'Desarrollo móvil con Android')), 'Tu primera pantalla', 2, 'Interfaces y navegación'),
((SELECT id_nivel FROM niveles WHERE nombre_nivel = 'Unidad 1' AND id_curso = (SELECT id_curso FROM cursos WHERE nombre_curso = 'Desarrollo móvil con Android')), 'Tu primer reto práctico', 3, 'Práctica');

-- 7. Ejercicios & Opciones
-- Leccion 2: Fundamentos de APIs (APIs con Spring Boot)
INSERT INTO ejercicios (id_leccion, tipo_ejercicio, enunciado, orden, respuesta_correcta, explicacion) VALUES
((SELECT id_leccion FROM lecciones WHERE titulo = 'Fundamentos de APIs'), 'OPCION_MULTIPLE', '¿Qué es una API?', 1, 'Un conjunto de reglas para que dos sistemas se comuniquen', 'Una API define cómo un sistema puede pedir información o acciones a otro. Por ejemplo, una app del clima consulta datos a un servicio.');

INSERT INTO opciones_ejercicio (id_ejercicio, texto_opcion, es_correcta) VALUES
((SELECT id_ejercicio FROM ejercicios WHERE enunciado = '¿Qué es una API?'), 'Una base de datos', false),
((SELECT id_ejercicio FROM ejercicios WHERE enunciado = '¿Qué es una API?'), 'Un conjunto de reglas para que dos sistemas se comuniquen', true),
((SELECT id_ejercicio FROM ejercicios WHERE enunciado = '¿Qué es una API?'), 'Un lenguaje de programación', false),
((SELECT id_ejercicio FROM ejercicios WHERE enunciado = '¿Qué es una API?'), 'Un servidor web', false);

INSERT INTO ejercicios (id_leccion, tipo_ejercicio, enunciado, orden, respuesta_correcta, explicacion) VALUES
((SELECT id_leccion FROM lecciones WHERE titulo = 'Fundamentos de APIs'), 'OPCION_MULTIPLE', '¿Qué método HTTP suele usarse para consultar información?', 2, 'GET', 'El método GET se utiliza para solicitar datos de un recurso específico sin modificar el servidor.');

INSERT INTO opciones_ejercicio (id_ejercicio, texto_opcion, es_correcta) VALUES
((SELECT id_ejercicio FROM ejercicios WHERE enunciado = '¿Qué método HTTP suele usarse para consultar información?'), 'GET', true),
((SELECT id_ejercicio FROM ejercicios WHERE enunciado = '¿Qué método HTTP suele usarse para consultar información?'), 'DELETE', false),
((SELECT id_ejercicio FROM ejercicios WHERE enunciado = '¿Qué método HTTP suele usarse para consultar información?'), 'PATCH', false);

INSERT INTO ejercicios (id_leccion, tipo_ejercicio, enunciado, orden, respuesta_correcta, explicacion) VALUES
((SELECT id_leccion FROM lecciones WHERE titulo = 'Fundamentos de APIs'), 'OPCION_MULTIPLE', 'La API responde con 404. ¿Qué significa?', 3, 'El servidor no encuentra el recurso solicitado', 'El código de estado 404 Not Found indica que el recurso solicitado no fue encontrado en el servidor.');

INSERT INTO opciones_ejercicio (id_ejercicio, texto_opcion, es_correcta) VALUES
((SELECT id_ejercicio FROM ejercicios WHERE enunciado = 'La API responde con 404. ¿Qué significa?'), 'La petición fue exitosa', false),
((SELECT id_ejercicio FROM ejercicios WHERE enunciado = 'La API responde con 404. ¿Qué significa?'), 'El servidor no encuentra el recurso solicitado', true),
((SELECT id_ejercicio FROM ejercicios WHERE enunciado = 'La API responde con 404. ¿Qué significa?'), 'Tu contraseña es incorrecta', false);

-- Ejercicios React (Componentes y props)
INSERT INTO ejercicios (id_leccion, tipo_ejercicio, enunciado, orden, respuesta_correcta, explicacion) VALUES
((SELECT id_leccion FROM lecciones WHERE titulo = 'Componentes y props'), 'OPCION_MULTIPLE', '¿Qué es un componente en React?', 1, 'Una función o clase que retorna elementos de UI', 'Los componentes son las piezas fundamentales reutilizables en React.');
INSERT INTO opciones_ejercicio (id_ejercicio, texto_opcion, es_correcta) VALUES
((SELECT id_ejercicio FROM ejercicios WHERE enunciado = '¿Qué es un componente en React?'), 'Una función o clase que retorna elementos de UI', true),
((SELECT id_ejercicio FROM ejercicios WHERE enunciado = '¿Qué es un componente en React?'), 'Una base de datos SQL', false);

-- Ejercicios MySQL (Tu primera consulta)
INSERT INTO ejercicios (id_leccion, tipo_ejercicio, enunciado, orden, respuesta_correcta, explicacion) VALUES
((SELECT id_leccion FROM lecciones WHERE titulo = 'Tu primera consulta'), 'OPCION_MULTIPLE', '¿Qué sentencia SQL se usa para consultar datos?', 1, 'SELECT', 'La sentencia SELECT recupera filas de una o más tablas.');
INSERT INTO opciones_ejercicio (id_ejercicio, texto_opcion, es_correcta) VALUES
((SELECT id_ejercicio FROM ejercicios WHERE enunciado = '¿Qué sentencia SQL se usa para consultar datos?'), 'SELECT', true),
((SELECT id_ejercicio FROM ejercicios WHERE enunciado = '¿Qué sentencia SQL se usa para consultar datos?'), 'UPDATE', false);

-- Ejercicios POO (Clases y objetos)
INSERT INTO ejercicios (id_leccion, tipo_ejercicio, enunciado, orden, respuesta_correcta, explicacion) VALUES
((SELECT id_leccion FROM lecciones WHERE titulo = 'Clases y objetos'), 'OPCION_MULTIPLE', '¿Qué es una clase en POO?', 1, 'Una plantilla para crear objetos', 'Una clase define los atributos y métodos que tendrán las instancias u objetos.');
INSERT INTO opciones_ejercicio (id_ejercicio, texto_opcion, es_correcta) VALUES
((SELECT id_ejercicio FROM ejercicios WHERE enunciado = '¿Qué es una clase en POO?'), 'Una plantilla para crear objetos', true),
((SELECT id_ejercicio FROM ejercicios WHERE enunciado = '¿Qué es una clase en POO?'), 'Un archivo binario ejecutable', false);

-- Ejercicios Android (Tu primera pantalla)
INSERT INTO ejercicios (id_leccion, tipo_ejercicio, enunciado, orden, respuesta_correcta, explicacion) VALUES
((SELECT id_leccion FROM lecciones WHERE titulo = 'Tu primera pantalla'), 'OPCION_MULTIPLE', '¿Qué componente define el diseño visual en Android clásico?', 1, 'Layout XML', 'Los archivos XML en res/layout definen la interfaz de usuario.');
INSERT INTO opciones_ejercicio (id_ejercicio, texto_opcion, es_correcta) VALUES
((SELECT id_ejercicio FROM ejercicios WHERE enunciado = '¿Qué componente define el diseño visual en Android clásico?'), 'Layout XML', true),
((SELECT id_ejercicio FROM ejercicios WHERE enunciado = '¿Qué componente define el diseño visual en Android clásico?'), 'Gradle script', false);


-- Ejercicios ENTREVISTA para "Tu primer reto práctico" (APIs con Spring Boot)
INSERT INTO ejercicios (id_leccion, tipo_ejercicio, enunciado, orden, respuesta_correcta, explicacion) VALUES
((SELECT id_leccion FROM lecciones WHERE titulo = 'Tu primer reto práctico' AND id_nivel = (SELECT id_nivel FROM niveles WHERE id_curso = (SELECT id_curso FROM cursos WHERE nombre_curso = 'APIs con Spring Boot'))), 'ENTREVISTA', '¿Cómo explicarías una API a alguien sin experiencia técnica?', 1, NULL, 'Explica que conecta dos sistemas.
Describe una petición y una respuesta.
Añade un ejemplo: consultar el clima.
Evita términos que no hayas explicado.'),

((SELECT id_leccion FROM lecciones WHERE titulo = 'Tu primer reto práctico' AND id_nivel = (SELECT id_nivel FROM niveles WHERE id_curso = (SELECT id_curso FROM cursos WHERE nombre_curso = 'APIs con Spring Boot'))), 'ENTREVISTA', 'Tu app no recibe datos de una API. ¿Qué revisarías primero?', 2, NULL, 'Revisa la conexión y la URL.
Comprueba el método y los parámetros.
Mira el código de respuesta y el error.
Revisa la autenticación si corresponde.'),

((SELECT id_leccion FROM lecciones WHERE titulo = 'Tu primer reto práctico' AND id_nivel = (SELECT id_nivel FROM niveles WHERE id_curso = (SELECT id_curso FROM cursos WHERE nombre_curso = 'APIs con Spring Boot'))), 'ENTREVISTA', 'Cuéntame sobre un problema técnico que hayas resuelto.', 3, NULL, 'Describe el contexto y el problema.
Explica tu aporte concreto.
Cuenta el resultado y lo aprendido.');

-- 8. Progreso Lección
-- Luis: 7 días consecutivos terminando hoy
INSERT INTO progreso_leccion (id_usuario, id_leccion, aciertos, total_preguntas, xp_ganado, fecha_completado, uuid_cliente) VALUES
((SELECT id_usuario FROM usuarios WHERE correo = 'luis@correo.com'), (SELECT id_leccion FROM lecciones WHERE titulo = 'Primeros pasos' AND id_nivel = (SELECT id_nivel FROM niveles WHERE id_curso = (SELECT id_curso FROM cursos WHERE nombre_curso = 'APIs con Spring Boot'))), 3, 3, 30, CURRENT_TIMESTAMP - INTERVAL '6 days', gen_random_uuid()),
((SELECT id_usuario FROM usuarios WHERE correo = 'luis@correo.com'), (SELECT id_leccion FROM lecciones WHERE titulo = 'Primeros pasos' AND id_nivel = (SELECT id_nivel FROM niveles WHERE id_curso = (SELECT id_curso FROM cursos WHERE nombre_curso = 'APIs con Spring Boot'))), 3, 3, 30, CURRENT_TIMESTAMP - INTERVAL '5 days', gen_random_uuid()),
((SELECT id_usuario FROM usuarios WHERE correo = 'luis@correo.com'), (SELECT id_leccion FROM lecciones WHERE titulo = 'Primeros pasos' AND id_nivel = (SELECT id_nivel FROM niveles WHERE id_curso = (SELECT id_curso FROM cursos WHERE nombre_curso = 'APIs con Spring Boot'))), 3, 3, 30, CURRENT_TIMESTAMP - INTERVAL '4 days', gen_random_uuid()),
((SELECT id_usuario FROM usuarios WHERE correo = 'luis@correo.com'), (SELECT id_leccion FROM lecciones WHERE titulo = 'Primeros pasos' AND id_nivel = (SELECT id_nivel FROM niveles WHERE id_curso = (SELECT id_curso FROM cursos WHERE nombre_curso = 'APIs con Spring Boot'))), 3, 3, 30, CURRENT_TIMESTAMP - INTERVAL '3 days', gen_random_uuid()),
((SELECT id_usuario FROM usuarios WHERE correo = 'luis@correo.com'), (SELECT id_leccion FROM lecciones WHERE titulo = 'Primeros pasos' AND id_nivel = (SELECT id_nivel FROM niveles WHERE id_curso = (SELECT id_curso FROM cursos WHERE nombre_curso = 'APIs con Spring Boot'))), 3, 3, 30, CURRENT_TIMESTAMP - INTERVAL '2 days', gen_random_uuid()),
((SELECT id_usuario FROM usuarios WHERE correo = 'luis@correo.com'), (SELECT id_leccion FROM lecciones WHERE titulo = 'Primeros pasos' AND id_nivel = (SELECT id_nivel FROM niveles WHERE id_curso = (SELECT id_curso FROM cursos WHERE nombre_curso = 'APIs con Spring Boot'))), 3, 3, 30, CURRENT_TIMESTAMP - INTERVAL '1 days', gen_random_uuid()),
((SELECT id_usuario FROM usuarios WHERE correo = 'luis@correo.com'), (SELECT id_leccion FROM lecciones WHERE titulo = 'Primeros pasos' AND id_nivel = (SELECT id_nivel FROM niveles WHERE id_curso = (SELECT id_curso FROM cursos WHERE nombre_curso = 'APIs con Spring Boot'))), 3, 3, 30, CURRENT_TIMESTAMP, gen_random_uuid());

-- Ana y Juan: registros de prueba
INSERT INTO progreso_leccion (id_usuario, id_leccion, aciertos, total_preguntas, xp_ganado, fecha_completado, uuid_cliente) VALUES
((SELECT id_usuario FROM usuarios WHERE correo = 'ana@correo.com'), (SELECT id_leccion FROM lecciones WHERE titulo = 'Primeros pasos' AND id_nivel = (SELECT id_nivel FROM niveles WHERE id_curso = (SELECT id_curso FROM cursos WHERE nombre_curso = 'Desarrollo web con React'))), 3, 3, 30, CURRENT_TIMESTAMP - INTERVAL '2 hours', gen_random_uuid()),
((SELECT id_usuario FROM usuarios WHERE correo = 'ana@correo.com'), (SELECT id_leccion FROM lecciones WHERE titulo = 'Componentes y props' AND id_nivel = (SELECT id_nivel FROM niveles WHERE id_curso = (SELECT id_curso FROM cursos WHERE nombre_curso = 'Desarrollo web con React'))), 2, 2, 20, CURRENT_TIMESTAMP - INTERVAL '1 hours', gen_random_uuid()),
((SELECT id_usuario FROM usuarios WHERE correo = 'juan@correo.com'), (SELECT id_leccion FROM lecciones WHERE titulo = 'Primeros pasos' AND id_nivel = (SELECT id_nivel FROM niveles WHERE id_curso = (SELECT id_curso FROM cursos WHERE nombre_curso = 'Bases de datos con MySQL'))), 3, 3, 30, CURRENT_TIMESTAMP - INTERVAL '8 hours', gen_random_uuid());
