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
-- ============================================================

SET client_encoding = 'UTF8';
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

-- ---------- 2. Profesiones (= puestos/rutas a las que se postula) ----------
INSERT INTO profesiones (nombre, descripcion) VALUES
('Desarrollo Web', 'Creación y mantenimiento de sitios y aplicaciones web'),
('English Academy', 'Aprendizaje y dominio técnico y conversacional del idioma inglés'),
('Marketing Digital', 'Estrategias de promoción, posicionamiento SEO/SEM y publicidad en medios digitales'),
('Data Science', 'Análisis de datos, minería, estadística y aprendizaje automático'),
('Ciberseguridad', 'Protección de infraestructuras, redes y datos contra amenazas y ataques digitales'),
('Liderazgo y Habilidades Blandas', 'Desarrollo personal, comunicación efectiva y gestión de equipos de trabajo'),
('Diseño de Producto y UX', 'Investigación, creación de interfaces y mejora continua de la experiencia de usuario'),
('Contenido Audiovisual', 'Producción, edición y postproducción de video, audio y formatos multimedia'),
('Desarrollo Móvil', 'Creación y despliegue de aplicaciones nativas e híbridas para iOS y Android'),
('AI Academy', 'Fundamentos y aplicación de inteligencia artificial, modelos generativos y redes neuronales'),
('Blockchain y Web3', 'Desarrollo sobre tecnologías descentralizadas, criptografía y contratos inteligentes'),
('Programación', 'Fundamentos de lógica algorítmica, estructuras de datos y lenguajes de código'),
('Recursos Humanos', 'Gestión del talento, procesos de reclutamiento y desarrollo de cultura organizacional'),
('Startups', 'Metodologías para la creación, escalamiento y levantamiento de capital de empresas emergentes'),
('Negocios', 'Estrategia corporativa, optimización de ventas y gestión administrativa empresarial'),
('Finanzas e Inversiones', 'Análisis de mercados financieros, manejo de capital y economía personal/corporativa'),
('Cloud Computing y DevOps', 'Arquitectura e infraestructura en la nube, despliegue continuo y operaciones automatizadas'),
('Diseño Gráfico y Arte Digital', 'Creación de identidad visual, ilustración, branding y comunicación gráfica');

INSERT INTO usuario_profesiones (id_usuario, id_profesion, principal)
SELECT u.id_usuario, p.id_profesion, TRUE
FROM (VALUES ('alumno@techmentor.test', 'Desarrollo Web'),
             ('vip@techmentor.test',    'Desarrollo Móvil')) AS v(email, profesion)
JOIN usuarios u ON u.email = v.email
JOIN profesiones p ON p.nombre = v.profesion;

-- ---------- 3. Categorías y Cursos ----------
-- Aseguramos que existan las categorías necesarias antes de insertar los cursos
INSERT INTO categorias (nombre, descripcion, icono) VALUES
('Desarrollo Web', 'Creación y mantenimiento de sitios y aplicaciones web', 'https://static.platzi.com/media/learningpath/emblems/80b010b7-adb8-4274-965d-113d97cb0d5b.jpg'),
('English Academy', 'Aprendizaje y dominio técnico y conversacional del idioma inglés', 'https://static.platzi.com/media/learningpath/emblems/1091fc90-8f1e-41f8-8b71-236b9004c992.jpg'),
('Marketing Digital', 'Estrategias de promoción, posicionamiento SEO/SEM y publicidad en medios digitales', 'https://static.platzi.com/media/learningpath/emblems/ab53fae1-8f04-40de-923b-393f30ac1eea.jpg'),
('Data Science', 'Análisis de datos, minería, estadística y aprendizaje automático', 'https://static.platzi.com/media/learningpath/emblems/e1f9ffbf-0fbc-4a5e-a07c-143f82c1a850.jpg'),
('Ciberseguridad', 'Protección de infraestructuras, redes y datos contra amenazas y ataques digitales', 'https://static.platzi.com/media/learningpath/emblems/e4b3237a-7cf4-46dc-8611-cd3fed1a7774.jpg'),
('Liderazgo y Habilidades Blandas', 'Desarrollo personal, comunicación efectiva y gestión de equipos de trabajo', 'https://static.platzi.com/media/learningpath/emblems/28efe215-abbe-421e-be01-e97e50641652.jpg'),
('Diseño de Producto y UX', 'Investigación, creación de interfaces y mejora continua de la experiencia de usuario', 'https://static.platzi.com/media/learningpath/emblems/d7a6f6fb-9afc-451f-b21a-723c2beaa2ef.jpg'),
('Contenido Audiovisual', 'Producción, edición y postproducción de video, audio y formatos multimedia', 'https://static.platzi.com/media/learningpath/emblems/2b1e9410-8650-45b8-99c7-7bbbf25576fc.jpg'),
('Desarrollo Móvil', 'Creación y despliegue de aplicaciones nativas e híbridas para iOS y Android', 'https://static.platzi.com/media/learningpath/emblems/9932021b-404b-4415-a6ea-f3a93855f08e.jpg'),
('AI Academy', 'Fundamentos y aplicación de inteligencia artificial, modelos generativos y redes neuronales', 'https://static.platzi.com/media/schools/emblems/Badges_AI_Academy.png'),
('Blockchain y Web3', 'Desarrollo sobre tecnologías descentralizadas, criptografía y contratos inteligentes', 'https://static.platzi.com/media/learningpath/emblems/f106aa88-12d8-437b-a72b-e8f30df49d81.jpg'),
('Programación', 'Fundamentos de lógica algorítmica, estructuras de datos y lenguajes de código', 'https://static.platzi.com/media/learningpath/emblems/2e48edd1-0888-445a-a022-93de8743d965.jpg'),
('Recursos Humanos', 'Gestión del talento, procesos de reclutamiento y desarrollo de cultura organizacional', 'https://static.platzi.com/media/learningpath/emblems/8af138ab-6106-4ed6-9a3f-9db71e3e6a4c.jpg'),
('Startups', 'Metodologías para la creación, escalamiento y levantamiento de capital de empresas emergentes', 'https://static.platzi.com/media/learningpath/emblems/569972d1-c9c5-4b43-bb9f-8383394756f4.jpg'),
('Negocios', 'Estrategia corporativa, optimización de ventas y gestión administrativa empresarial', 'https://static.platzi.com/media/learningpath/emblems/63b5a5d0-b1ab-49e9-a367-06f773aa01ab.jpg'),
('Finanzas e Inversiones', 'Análisis de mercados financieros, manejo de capital y economía personal/corporativa', 'https://static.platzi.com/media/learningpath/emblems/0ee14a84-15d1-40bf-9a83-1307b71d32f3.jpg'),
('Cloud Computing y DevOps', 'Arquitectura e infraestructura en la nube, despliegue continuo y operaciones automatizadas', 'https://static.platzi.com/media/learningpath/emblems/2935507d-bb87-4661-b17c-dff68b4c0aab.jpg'),
('Diseño Gráfico y Arte Digital', 'Creación de identidad visual, ilustración, branding y comunicación gráfica', 'https://static.platzi.com/media/learningpath/emblems/2a30633f-3473-443d-b665-0f8512a79e59.jpg');

INSERT INTO cursos (id_categoria, nombre, descripcion, xp_recompensa)
SELECT c.id_categoria, v.curso, v.descripcion, v.xp
FROM (VALUES
    -- Categoría de Desarrollo Web
    ('Desarrollo Web',   'Curso Definitivo de HTML y CSS',                   'Aprende a estructurar y dar estilo a páginas web desde cero',                 325),
    ('Desarrollo Web',   'Curso Práctico de HTML y CSS',                     'Aplica tus conocimientos creando un proyecto web real',                       415),
    ('Desarrollo Web',   'Curso de CSS Grid Básico',                         'Domina el sistema de grillas para layouts complejos',                         385),
    ('Desarrollo Web',   'Curso de CSS Flexbox',                             'Alinea y distribuye elementos fácilmente en tus interfaces',                  440),
    ('Desarrollo Web',   'Curso Básico de JavaScript',                       'Iníciate en el lenguaje principal de la web y crea interactividad',           350),
    ('Desarrollo Web',   'Curso Práctico de JavaScript',                     'Resuelve problemas lógicos y manipula el DOM con JS',                         495),
    ('Desarrollo Web',   'Curso de Asincronismo con JavaScript',             'Entiende callbacks, promesas y async/await a profundidad',                    310),
    ('Desarrollo Web',   'Curso de ECMAScript: Historia y Versiones',        'Conoce la evolución y nuevas características de JavaScript',                  475),
    ('Desarrollo Web',   'Curso de Frontend Developer',                      'Domina la creación de interfaces interactivas y adaptables',                  400),
    ('Desarrollo Web',   'Curso Práctico de Frontend Developer',             'Desarrolla el maquetado completo de una tienda online',                       335),
    ('Desarrollo Web',   'Curso de React.js Básico',                         'Construye aplicaciones declarativas basadas en componentes',                  460),
    ('Desarrollo Web',   'Curso de Next.js',                                 'Crea aplicaciones React optimizadas para SEO y rendimiento',                  315),
    ('Desarrollo Web',   'Curso de Vue.js Básico',                           'Aprende el framework progresivo para interfaces de usuario',                  425),
    ('Desarrollo Web',   'Curso de Angular Básico',                          'Desarrolla aplicaciones escalables con el framework de Google',               370),
    ('Desarrollo Web',   'Curso de Backend con Node.js: API REST',           'Crea servidores y expón datos mediante endpoints con Express',                485),
    ('Desarrollo Web',   'Curso de Backend con Node.js y PostgreSQL',        'Conecta tu API a bases de datos relacionales robustas',                       345),
    ('Desarrollo Web',   'Curso de Webpack',                                 'Empaqueta y optimiza los recursos de tu aplicación web',                      390),

    -- Categoría de Desarrollo Móvil
    ('Desarrollo Móvil', 'Curso de Introducción a React Native',             'Crea aplicaciones multiplataforma nativas con React',                         455),
    ('Desarrollo Móvil', 'Curso Práctico de React Native',                   'Desarrolla una app móvil completa desde cero hasta producción',               305),
    ('Desarrollo Móvil', 'Curso de Kotlin Básico',                           'Aprende el lenguaje oficial y moderno para desarrollo nativo en Android',     420),
    ('Desarrollo Móvil', 'Curso de Desarrollo de Apps con Android y Kotlin', 'Construye interfaces y lógica para dispositivos Android',                     380),
    ('Desarrollo Móvil', 'Curso de Introducción a Swift',                    'Iníciate en el lenguaje de Apple para el ecosistema iOS',                     470),
    ('Desarrollo Móvil', 'Curso de Desarrollo de Apps con iOS y Swift',      'Crea tu primera aplicación nativa para iPhone y iPad',                        360),
    ('Desarrollo Móvil', 'Curso Básico de Flutter',                          'Desarrolla aplicaciones nativas para iOS y Android con un solo código',       490),
    ('Desarrollo Móvil', 'Curso Práctico de Flutter',                        'Aplica widgets complejos y gestión del estado en Dart',                       325),
    ('Desarrollo Móvil', 'Curso de Jetpack Compose',                         'Crea interfaces de usuario declarativas y modernas en Android',               435),
    ('Desarrollo Móvil', 'Curso de SwiftUI',                                 'Domina el nuevo paradigma para crear interfaces en ecosistemas Apple',        355),

    -- Categoría de Inglés
    ('Inglés',           'Curso de Inglés Básico A1',                        'Adquiere vocabulario y estructuras elementales para el día a día',            410),
    ('Inglés',           'Curso de Inglés Básico A2',                        'Amplía tu vocabulario y aprende a describir experiencias pasadas',            340),
    ('Inglés',           'Curso de Inglés Intermedio B1',                    'Mejora tu fluidez y comprensión para comunicarte en el entorno laboral',      465),
    ('Inglés',           'Curso de Inglés Intermedio B2',                    'Domina estructuras complejas y argumentación en inglés',                      315),
    ('Inglés',           'Curso de Inglés Avanzado C1',                      'Perfecciona tu pronunciación y comprensión de modismos nativos',              480),
    ('Inglés',           'Curso de Inglés para Entrevistas de Trabajo',      'Prepárate para destacar y responder con total seguridad en inglés',           395),
    ('Inglés',           'Curso de Inglés para Programadores',               'Vocabulario técnico y redacción de documentación de código',                  445),
    ('Inglés',           'Curso de Pronunciación en Inglés',                 'Reduce tu acento y comunícate con mayor claridad oral',                       375),
    ('Inglés',           'Curso de Inglés para Negocios',                    'Redacta correos, haz presentaciones y negocia en inglés corporativo',         500),

    -- Categoría de Programación y Tecnología
    ('Programación',     'Curso Gratis de Programación Básica',              'Descubre cómo funciona la web y escribe tus primeras líneas de código',       330),
    ('Programación',     'Curso de Pensamiento Lógico',                      'Desarrolla habilidades analíticas para la resolución de algoritmos',          450),
    ('Programación',     'Curso de Estructuras de Datos',                    'Comprende arrays, listas, árboles y grafos en memoria',                       365),
    ('Programación',     'Curso de Fundamentos de Ingeniería de Software',   'Conoce las bases para diseñar, estructurar y mantener software profesional',  495),
    ('Programación',     'Curso de Introducción a la Terminal',              'Navega y administra tu sistema operativo mediante línea de comandos',         320),
    ('Programación',     'Curso Profesional de Git y GitHub',                'Domina el control de versiones y el trabajo colaborativo en código',          425),
    ('Programación',     'Curso de Programación Orientada a Objetos',        'Aprende los pilares de clases, herencia, polimorfismo y encapsulamiento',     385),
    ('Programación',     'Curso de Bases de Datos Relacionales',             'Entiende el modelado entidad-relación y normalización de datos',              440),
    ('Programación',     'Curso de Matemáticas Discretas',                   'Fundamentos matemáticos esenciales para la lógica computacional',             310)
) AS v(categoria, curso, descripcion, xp)
JOIN categorias c ON c.nombre = v.categoria;

-- Niveles (para los 3 primeros cursos de cada escuela):
INSERT INTO niveles (id_curso, nombre, descripcion, orden)
SELECT c.id_curso, n.nombre, n.descripcion, n.orden
FROM cursos c
JOIN (VALUES 
    ('Básico', 'Fundamentos esenciales y teóricos para dar los primeros pasos en la materia.', 1), 
    ('Intermedio', 'Profundización técnica, herramientas adicionales y aplicación práctica estructurada.', 2), 
    ('Avanzado', 'Resolución de problemas complejos, buenas prácticas y casos de uso a nivel profesional.', 3)
) AS n(nombre, descripcion, orden)
  ON c.nombre IN (
      -- Desarrollo Web
      'Curso Definitivo de HTML y CSS',
      'Curso Práctico de HTML y CSS',
      'Curso de CSS Grid Básico',
      
      -- Desarrollo Móvil
      'Curso de Introducción a React Native',
      'Curso Práctico de React Native',
      'Curso de Kotlin Básico',
      
      -- Inglés
      'Curso de Inglés Básico A1',
      'Curso de Inglés Básico A2',
      'Curso de Inglés Intermedio B1',
      
      -- Programación
      'Curso Gratis de Programación Básica',
      'Curso de Pensamiento Lógico',
      'Curso de Estructuras de Datos'
  );

-- ---------- 4. Lecciones (30 lecciones para 'Curso Definitivo de HTML y CSS') ----------
INSERT INTO lecciones (id_nivel, titulo, descripcion, contenido, orden, xp_recompensa)
SELECT n.id_nivel, v.titulo, v.descripcion, v.contenido, v.orden_leccion, 20
FROM niveles n
JOIN cursos c ON n.id_curso = c.id_curso
JOIN (VALUES
    -- NIVEL 1: BÁSICO (orden_nivel = 1)
    (1, 1, 'Introducción a la Web', 'Cómo funciona internet y los navegadores.', 'Bienvenidos a la primera lección oficial del curso. Para entender cómo construir para la web, primero debemos comprender qué es exactamente y cómo viaja la información desde un servidor remoto hasta la pantalla de tu computadora. A menudo confundimos Internet con la World Wide Web, pero son dos cosas distintas. Internet es la inmensa infraestructura global de redes de computadoras interconectadas mediante cables submarinos, satélites y fibra óptica. Es la carretera física. La Web, por otro lado, es un servicio que circula por esa carretera, un inmenso conjunto de documentos digitales conectados mediante hipervínculos. El pilar fundamental de la Web es el modelo Cliente-Servidor. Imagina un restaurante: tú eres el cliente que hace un pedido y el cocinero en la cocina es el servidor que prepara y entrega la comida. En el mundo digital, el cliente es tu navegador web y el servidor es una computadora de alta potencia, ubicada en un centro de datos, que almacena los archivos del sitio web al que quieres acceder. Cuando escribes una dirección web y presionas Enter, desencadenas una serie de eventos a la velocidad de la luz. Tu navegador no sabe dónde está esa página, ya que las computadoras se comunican mediante direcciones IP. Para solucionarlo, hace una consulta a un servidor DNS que actúa como la libreta de contactos de internet, traduciendo el nombre legible en una dirección IP exacta. Una vez localizada, tu navegador envía una petición HTTP al servidor. Esta petición es un mensaje que solicita el documento principal. Si todo está en orden, el servidor responde con un código de estado 200 OK y comienza a enviar paquetes de datos de regreso. El primer archivo en llegar es siempre el documento HTML. Aquí es donde entra en juego el motor de renderizado de tu navegador. Cuando recibe el código de texto plano, comienza a leerlo línea por línea. A medida que lee las etiquetas, construye un modelo interno en la memoria llamado DOM, que es el árbol genealógico de todos los elementos de la página. Mientras construye este árbol, se encuentra con enlaces a recursos externos como hojas de estilo CSS, imágenes y archivos JavaScript, realizando peticiones secundarias para traerlos. Cuando el CSS llega, crea otro árbol paralelo llamado CSSOM que contiene todas las reglas de diseño. Luego, el navegador fusiona el DOM y el CSSOM para crear el Render Tree. Con este árbol listo, el motor calcula el espacio matemático exacto que ocupará cada elemento en tu pantalla de acuerdo al dispositivo, y finalmente ejecuta el proceso de pintado inyectando los colores en los píxeles de tu monitor. Comprender este ciclo de petición, respuesta y renderizado es lo que te permite escribir código optimizado y eficiente.'),
    (1, 2, '¿Qué es HTML?', 'Estructura base de un documento HTML5.', 'Desglose de las etiquetas <html>, <head>, <body> y el DOCTYPE.'),
    (1, 3, 'Etiquetas de texto', 'Títulos y párrafos.', 'Uso correcto de la jerarquía de encabezados (h1 a h6) y la etiqueta de párrafo (p).'),
    (1, 4, 'Enlaces y navegación', 'Conectando internet.', 'Uso de la etiqueta <a>, el atributo href y la apertura de pestañas con target="_blank".'),
    (1, 5, 'Inserción de imágenes', 'Multimedia básica.', 'Implementación de la etiqueta <img>, rutas relativas vs absolutas y el atributo alt para accesibilidad.'),
    (1, 6, 'Listas HTML', 'Listas ordenadas y desordenadas.', 'Diferencia entre <ul> y <ol>, y cómo usar los items <li>.'),
    (1, 7, 'Introducción a CSS', 'Separando estructura de diseño.', 'Qué son las hojas de estilo y las 3 formas de integrarlas (en línea, en el head, archivo externo).'),
    (1, 8, 'Selectores básicos', 'Apuntando a elementos HTML.', 'Cómo usar selectores de etiqueta, de clase (.) y de ID (#).'),
    (1, 9, 'Colores y fondos', 'Pintando la web.', 'Uso de color, background-color y los formatos hexadecimal y RGB.'),
    (1, 10, 'Propiedades de texto', 'Tipografía en CSS.', 'Modificación de fuentes con font-family, font-size, font-weight y text-align.'),

    -- NIVEL 2: INTERMEDIO (orden_nivel = 2)
    (2, 1, 'El Modelo de Caja', 'El concepto más importante de CSS.', 'Explicación visual del Box Model: Content, Padding, Border y Margin.'),
    (2, 2, 'Márgenes en CSS', 'Separación externa.', 'Uso de margin, márgenes negativos y el comportamiento de colapso de márgenes.'),
    (2, 3, 'Padding y Bordes', 'Separación interna y límites.', 'Uso de padding, border-style, border-radius y el impacto de box-sizing: border-box.'),
    (2, 4, 'Propiedad Display', 'Comportamiento en bloque o línea.', 'Diferencias clave entre display: block, inline, inline-block y none.'),
    (2, 5, 'Posicionamiento Estático y Relativo', 'Control del flujo de la página.', 'Cómo afecta position: relative al elemento sin sacarlo del flujo normal.'),
    (2, 6, 'Posicionamiento Absoluto y Fijo', 'Elementos flotantes.', 'Uso de position: absolute (respecto al ancestro) y position: fixed (respecto a la ventana).'),
    (2, 7, 'Jerarquía y Especificidad', 'Quién gana cuando hay conflicto.', 'Cómo calcula el navegador qué regla CSS aplicar basada en el peso del selector.'),
    (2, 8, 'Unidades de medida relativas', 'Más allá de los píxeles.', 'Cuándo y por qué usar em, rem y porcentajes en lugar de px.'),
    (2, 9, 'Variables en CSS', 'Custom properties.', 'Declaración de variables en el :root para temas de color y consistencia.'),
    (2, 10, 'Semántica HTML', 'Más allá de los divs.', 'Uso correcto de <header>, <nav>, <main>, <article>, <section> y <footer>.'),

    -- NIVEL 3: AVANZADO (orden_nivel = 3)
    (3, 1, 'Formularios en HTML', 'Recopilando datos del usuario.', 'Estructura con <form>, <input>, <label> y el atributo for.'),
    (3, 2, 'Tipos de inputs', 'Validación nativa del navegador.', 'Uso de inputs tipo email, password, date y atributos como required y placeholder.'),
    (3, 3, 'Pseudo-clases', 'Estilizando estados.', 'Uso de :hover, :focus, :active, :nth-child y :disabled.'),
    (3, 4, 'Introducción a Flexbox', 'Layouts unidimensionales.', 'Activación con display: flex, flex-direction y comportamiento de los flex-items.'),
    (3, 5, 'Alineación con Flexbox', 'Centrando elementos fácilmente.', 'Dominando justify-content, align-items y gap.'),
    (3, 6, 'Introducción a CSS Grid', 'Layouts bidimensionales.', 'Activación con display: grid y definición de filas y columnas con grid-template.'),
    (3, 7, 'Posicionamiento en Grid', 'Control total de la grilla.', 'Ubicación de elementos específicos usando grid-column y grid-row.'),
    (3, 8, 'Media Queries', 'Bases del Responsive Design.', 'Uso de la regla @media para adaptar el diseño a móviles, tablets y escritorio.'),
    (3, 9, 'Transiciones CSS', 'Movimientos suaves.', 'Animación de propiedades de estado a estado usando la propiedad transition.'),
    (3, 10, 'Animaciones con Keyframes', 'Movimiento complejo.', 'Creación de animaciones autónomas usando @keyframes y animation.')
) AS v(orden_nivel, orden_leccion, titulo, descripcion, contenido)
  ON n.orden = v.orden_nivel
WHERE c.nombre = 'Curso Definitivo de HTML y CSS';

-- ---------- 5. Recursos por lección ----------
INSERT INTO recursos_leccion (id_leccion, tipo, titulo, url, contenido, plataforma, duracion_segundos, orden)
SELECT l.id_leccion, v.tipo, v.titulo, v.url, v.contenido, v.plataforma, v.duracion_segundos, v.orden_recurso
FROM lecciones l
JOIN niveles n ON l.id_nivel = n.id_nivel
JOIN cursos c ON n.id_curso = c.id_curso
JOIN (VALUES
    -- RECURSOS PARA LECCIÓN 1: Introducción a la Web (orden_leccion = 1)
    (1, 'VIDEO', 'Clase 1: Cómo funciona la Web', 'https://www.youtube.com/watch?v=video_intro_web', NULL, 'YOUTUBE', 420, 1),
    (1, 'TEXTO', 'Apuntes: El modelo Cliente-Servidor', NULL, 'La Web funciona mediante peticiones y respuestas. Tu navegador (cliente) hace una petición HTTP al servidor, y este responde entregando los archivos HTML, CSS y JS necesarios para renderizar la página.', NULL, NULL, 2),
    
    -- RECURSOS PARA LECCIÓN 2: ¿Qué es HTML? (orden_leccion = 2)
    (2, 'VIDEO', 'Clase 2: Estructura base de HTML5', 'https://www.youtube.com/watch?v=video_html_base', NULL, 'YOUTUBE', 550, 1),
    (2, 'ENLACE', 'Documentación MDN: Estructura HTML', 'https://developer.mozilla.org/es/docs/Learn/HTML/Introduction_to_HTML/Document_and_website_structure', NULL, NULL, NULL, 2),
    (2, 'TEXTO', 'Cheat Sheet: Etiquetas raíz', NULL, 'Nunca olvides incluir el <!DOCTYPE html> al inicio. Toda tu página debe ir envuelta en la etiqueta <html>, dividiéndose internamente en <head> (metadatos) y <body> (contenido visible).', NULL, NULL, 3),

    -- RECURSOS PARA LECCIÓN 3: Etiquetas de texto (orden_leccion = 3)
    (3, 'VIDEO', 'Clase 3: Jerarquía y Párrafos', 'https://www.youtube.com/watch?v=video_etiquetas_texto', NULL, 'YOUTUBE', 340, 1),
    (3, 'TEXTO', 'Buenas prácticas de SEO', NULL, 'Solo debe existir un <h1> por página, ya que representa el título principal del documento. No saltes jerarquías (por ejemplo, de un <h2> a un <h4> directamente), ya que esto perjudica la accesibilidad y el posicionamiento en buscadores.', NULL, NULL, 2)
) AS v(orden_leccion, tipo, titulo, url, contenido, plataforma, duracion_segundos, orden_recurso)
  ON l.orden = v.orden_leccion
WHERE c.nombre = 'Curso Definitivo de HTML y CSS' 
  AND n.orden = 1;

-- ---------- 6. Ejercicios y opciones ----------
WITH leccion_objetivo AS (
    SELECT l.id_leccion
    FROM lecciones l
    JOIN niveles n ON l.id_nivel = n.id_nivel
    JOIN cursos c ON n.id_curso = c.id_curso
    WHERE c.nombre = 'Curso Definitivo de HTML y CSS'
      AND n.orden = 1
      AND l.orden = 1
),
ejercicios_insertados AS (
    INSERT INTO ejercicios (id_leccion, tipo_ejercicio, enunciado, orden, explicacion, xp_recompensa)
    SELECT tl.id_leccion, v.tipo, v.enunciado, v.orden_ejercicio, v.explicacion, v.xp
    FROM leccion_objetivo tl
    CROSS JOIN (VALUES
        ('OPCION_MULTIPLE', 'En el modelo cliente-servidor, ¿cuál es la función principal de tu navegador web?', 1, 'El navegador actúa como el cliente: formula y envía una petición HTTP al servidor para obtener los recursos.', 15),
        ('OPCION_MULTIPLE', 'Cuando el servidor acepta la petición, ¿qué documentos suele devolver para estructurar y pintar la página?', 2, 'El servidor devuelve archivos interpretables por el frontend, fundamentalmente HTML (estructura), CSS (estilo) y JS (interactividad).', 15)
    ) AS v(tipo, enunciado, orden_ejercicio, explicacion, xp)
    RETURNING id_ejercicio, orden
)
INSERT INTO opciones_ejercicio (id_ejercicio, texto, es_correcta, orden)
SELECT ei.id_ejercicio, v.texto, v.es_correcta, v.orden_opcion
FROM ejercicios_insertados ei
JOIN (VALUES
    (1, 'Procesar el código backend de bases de datos.', FALSE, 1),
    (1, 'Actuar como el cliente y realizar peticiones HTTP al servidor.', TRUE, 2),
    (1, 'Almacenar permanentemente el código fuente del sitio.', FALSE, 3),

    (2, 'Únicamente código de texto plano para que el usuario lo lea.', FALSE, 1),
    (2, 'El panel de administración del servidor web.', FALSE, 2),
    (2, 'Los archivos estáticos de la interfaz: HTML, CSS y JavaScript.', TRUE, 3)
) AS v(orden_ejercicio, texto, es_correcta, orden_opcion)
ON ei.orden = v.orden_ejercicio;

-- ---------- 7. Diagnóstico adaptado por puesto ----------
INSERT INTO categorias_pregunta (nombre, descripcion) VALUES
('Fundamentos',    'Conceptos base de programación'),
('Lógica',         'Razonamiento y control de flujo'),
('Bases de datos', 'Consultas y modelado'),
('Interfaces web', 'HTML, CSS y frameworks de frontend');

INSERT INTO examenes_diagnostico (id_profesion, nombre, descripcion)
SELECT p.id_profesion, 'Diagnóstico ' || p.nombre, 'Examen de nivelación para la ruta de ' || p.nombre
FROM profesiones p WHERE p.nombre IN ('Programación', 'Desarrollo Web');

-- Programación: 6 preguntas
INSERT INTO preguntas_diagnostico (id_examen, id_categoria_pregunta, enunciado, nivel_dificultad, tipo_pregunta, puntaje, orden)
SELECT e.id_examen, cp.id_categoria_pregunta, q.enunciado, q.dificultad, 'OPCION_MULTIPLE', 1, q.orden
FROM examenes_diagnostico e
JOIN (VALUES
    ('Fundamentos',    '¿Qué palabra clave declara una constante en Java?',             'BASICO',     1),
    ('Fundamentos',    '¿Qué tipo de dato guarda enteros de 32 bits en Java?',          'BASICO',     2),
    ('Lógica',         '¿Qué estructura repite código mientras se cumpla una condición?','BASICO',    3),
    ('Lógica',         '¿Cuántas veces corre: for (int i = 0; i < 3; i++)?',            'INTERMEDIO', 4),
    ('Bases de datos', '¿Qué sentencia SQL obtiene filas de una tabla?',                'BASICO',     5),
    ('Bases de datos', '¿Qué cláusula filtra filas en una consulta SQL?',               'BASICO',     6)
) AS q(categoria, enunciado, dificultad, orden) ON TRUE
JOIN categorias_pregunta cp ON cp.nombre = q.categoria
WHERE e.nombre = 'Diagnóstico Programación';

-- Desarrollo Web: 3 preguntas
INSERT INTO preguntas_diagnostico (id_examen, id_categoria_pregunta, enunciado, nivel_dificultad, tipo_pregunta, puntaje, orden)
SELECT e.id_examen, cp.id_categoria_pregunta, q.enunciado, q.dificultad, 'OPCION_MULTIPLE', 1, q.orden
FROM examenes_diagnostico e
JOIN (VALUES
    ('Interfaces web', '¿Qué etiqueta HTML define un enlace?',                   'BASICO', 1),
    ('Interfaces web', '¿Qué lenguaje da estilo a una página web?',               'BASICO', 2),
    ('Lógica',         '¿Qué estructura permite decidir entre dos caminos?',      'BASICO', 3)
) AS q(categoria, enunciado, dificultad, orden) ON TRUE
JOIN categorias_pregunta cp ON cp.nombre = q.categoria
WHERE e.nombre = 'Diagnóstico Desarrollo Web';

-- Opciones: la 1 de cada pregunta es la correcta
INSERT INTO opciones_diagnostico (id_pregunta, texto, es_correcta, orden)
SELECT p.id_pregunta, o.texto, o.orden = 1, o.orden
FROM preguntas_diagnostico p
JOIN (VALUES (1, 'Correcta'), (2, 'Distractor A'), (3, 'Distractor B')) AS o(orden, texto) ON TRUE;

-- ---------- 8. Reglas de recomendación ----------
-- Qué cursos exige cada profesión/escuela
INSERT INTO profesion_cursos (id_profesion, id_curso, prioridad)
SELECT p.id_profesion, c.id_curso, v.prioridad
FROM (VALUES
    ('Desarrollo Web',   'Curso Definitivo de HTML y CSS',            1),
    ('Desarrollo Web',   'Curso Práctico de HTML y CSS',              2),
    ('Desarrollo Web',   'Curso de CSS Grid Básico',                  2),
    ('Desarrollo Móvil', 'Curso de Introducción a React Native',      1),
    ('Desarrollo Móvil', 'Curso de Kotlin Básico',                    2),
    ('Programación',     'Curso Gratis de Programación Básica',       1),
    ('Programación',     'Curso de Pensamiento Lógico',               2),
    ('Programación',     'Curso de Bases de Datos Relacionales',      2),
    ('English Academy',  'Curso de Inglés Básico A1',                 1),
    ('Data Science',     'Curso de Bases de Datos Relacionales',      1)
) AS v(profesion, curso, prioridad)
JOIN profesiones p ON p.nombre = v.profesion
JOIN cursos c ON c.nombre = v.curso;

-- Qué curso refuerza cada categoría de pregunta (umbral = % mínimo esperado)
INSERT INTO categoria_pregunta_cursos (id_categoria_pregunta, id_curso, umbral_porcentaje)
SELECT cp.id_categoria_pregunta, c.id_curso, v.umbral
FROM (VALUES
    ('Fundamentos',    'Curso Gratis de Programación Básica', 60),
    ('Lógica',         'Curso de Pensamiento Lógico',         50),
    ('Bases de datos', 'Curso de Bases de Datos Relacionales',60),
    ('Interfaces web', 'Curso Definitivo de HTML y CSS',      60)
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
    biografia = 'Estudiante apasionado de desarrollo web y programación.',
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
WHERE c.nombre = 'Curso Definitivo de HTML y CSS'
ON CONFLICT (id_usuario, id_curso) DO UPDATE
SET estado = EXCLUDED.estado, xp_ganado = EXCLUDED.xp_ganado;

INSERT INTO progreso_curso (id_usuario, id_curso, id_nivel_actual, fecha_inicio, fecha_ultima_actividad, fecha_completado, estado, xp_ganado)
SELECT u.id_usuario, c.id_curso,
       (SELECT id_nivel FROM niveles WHERE id_curso = c.id_curso ORDER BY orden DESC LIMIT 1),
       CURRENT_TIMESTAMP - INTERVAL '10 days',
       CURRENT_TIMESTAMP - INTERVAL '1 day',
       CURRENT_TIMESTAMP - INTERVAL '1 day',
       'COMPLETADO',
       415
FROM cursos c
CROSS JOIN (SELECT id_usuario FROM usuarios WHERE email = 'alumno@techmentor.test') u
WHERE c.nombre = 'Curso Práctico de HTML y CSS'
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
WHERE c.nombre = 'Curso Definitivo de HTML y CSS' AND n.orden = 1 AND l.orden = 1
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

-- ---- Inscripciones a cursos con niveles existentes (determinista) ----
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
  AND EXISTS (SELECT 1 FROM niveles n WHERE n.id_curso = c.id_curso)
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