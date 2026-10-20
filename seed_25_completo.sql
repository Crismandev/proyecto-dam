-- ============================================================
-- TECHMENTOR - SCRIPT DE POBLACIÓN DE DATOS (POSTGRESQL / SUPABASE)
-- Promedio de 25+ registros proporcionales por cada tabla del sistema
-- Compatible con techmentor.sql (PostgreSQL 14+ y Supabase)
-- Ejecutar en el SQL Editor de Supabase o mediante psql.
-- ============================================================

SET client_encoding = 'UTF8';
CREATE EXTENSION IF NOT EXISTS pgcrypto;

-- ============================================================
-- 1. CATEGORÍAS (25 Categorías Tecnológicas y Profesionales)
-- ============================================================
INSERT INTO categorias (nombre, descripcion, icono, activo) VALUES
('Desarrollo Web', 'Creación y mantenimiento de sitios y aplicaciones web modernas', 'https://images.unsplash.com/photo-1547658719-da2b51169166?w=200', TRUE),
('Desarrollo Móvil', 'Creación y despliegue de aplicaciones nativas e híbridas para Android e iOS', 'https://images.unsplash.com/photo-1512941937669-90a1b58e7e9c?w=200', TRUE),
('Programación', 'Fundamentos de lógica algorítmica, estructuras de datos y lenguajes', 'https://images.unsplash.com/photo-1515879218367-8466d910aaa4?w=200', TRUE),
('English Academy', 'Dominio técnico y conversacional del idioma inglés para el sector tech', 'https://images.unsplash.com/photo-1546410531-bb4caa6b424d?w=200', TRUE),
('Data Science', 'Análisis de datos, minería estadística y aprendizaje automático', 'https://images.unsplash.com/photo-1551288049-bebda4e38f71?w=200', TRUE),
('AI Academy', 'Inteligencia artificial generativa, modelos de lenguaje y redes neuronales', 'https://images.unsplash.com/photo-1677442136019-21780efad99a?w=200', TRUE),
('Cloud Computing y DevOps', 'Arquitectura en la nube (AWS/GCP/Azure), CI/CD, Docker y Kubernetes', 'https://images.unsplash.com/photo-1451187580459-43490279c0fa?w=200', TRUE),
('Ciberseguridad', 'Hacking ético, auditoría de sistemas, pentesting y seguridad defensiva', 'https://images.unsplash.com/photo-1563986768609-322da13575f3?w=200', TRUE),
('Diseño de Producto y UX', 'Investigación de usuarios, Figma, prototipado rápido y diseño UI/UX', 'https://images.unsplash.com/photo-1581291518857-4e27b48ff24e?w=200', TRUE),
('Marketing Digital', 'Estrategias de crecimiento, SEO, SEM, métricas analíticas y campañas', 'https://images.unsplash.com/photo-1460925895917-afdab827c52f?w=200', TRUE),
('Base de Datos y Big Data', 'Diseño relacional PostgreSQL/MySQL, motores NoSQL y procesamiento distribuido', 'https://images.unsplash.com/photo-1544383835-bda2bc66a55d?w=200', TRUE),
('Calidad y Testing (QA)', 'Automatización de pruebas con Cypress, Selenium, JUnit y pruebas de carga', 'https://images.unsplash.com/photo-1516321318423-f06f85e504b3?w=200', TRUE),
('Arquitectura de Software', 'Patrones de diseño, microservicios, Event-Driven y Clean Architecture', 'https://images.unsplash.com/photo-1504384308090-c894fdcc538d?w=200', TRUE),
('Blockchain y Web3', 'Smart contracts con Solidity, redes Ethereum, DeFi y descentralización', 'https://images.unsplash.com/photo-1639762681485-074b7f938ba0?w=200', TRUE),
('Videojuegos y 3D', 'Desarrollo en Unity, Unreal Engine 5, C# y modelado tridimensional', 'https://images.unsplash.com/photo-1550745165-9bc0b252726f?w=200', TRUE),
('Liderazgo y Habilidades Blandas', 'Gestión de equipos, comunicación asertiva, resolución de conflictos', 'https://images.unsplash.com/photo-1522071820081-009f0129c71c?w=200', TRUE),
('Gestión Ágil y Scrum', 'Metodologías ágiles, Scrum Master, Kanban, Jira y gestión de proyectos', 'https://images.unsplash.com/photo-1531403009284-440f080d1e12?w=200', TRUE),
('Internet de las Cosas (IoT)', 'Sistemas embebidos, Arduino, ESP32, Raspberry Pi y domótica conectada', 'https://images.unsplash.com/photo-1518770660439-4636190af475?w=200', TRUE),
('Inteligencia de Negocios (BI)', 'Power BI, Tableau, ETL, dashboards corporativos y toma de decisiones', 'https://images.unsplash.com/photo-1551836022-d5d88e9218df?w=200', TRUE),
('Startups y Emprendimiento', 'Lean Startup, levantamiento de capital, validación de MVP y modelos SaaS', 'https://images.unsplash.com/photo-1559136555-9303baea8ebd?w=200', TRUE),
('Negocios y Finanzas', 'Estrategia corporativa, economía digital, finanzas y presupuestos', 'https://images.unsplash.com/photo-1454165804606-c3d57bc86b40?w=200', TRUE),
('Contenido Audiovisual', 'Edición en Premiere Pro, After Effects, locución y streaming profesional', 'https://images.unsplash.com/photo-1574717024653-61fd2cf4d44d?w=200', TRUE),
('Recursos Humanos Tech', 'Reclutamiento de perfiles IT, gestión del talento técnico y onboarding', 'https://images.unsplash.com/photo-1521791136064-7986c2920216?w=200', TRUE),
('Diseño Gráfico y Branding', 'Identidad de marca, Illustrator, Photoshop, tipografía y composición', 'https://images.unsplash.com/photo-1626785774573-4b799315345d?w=200', TRUE),
('Hardware y Redes', 'Protocolos TCP/IP, switches Cisco, enrutamiento, DNS y topologías físicas', 'https://images.unsplash.com/photo-1558494949-ef010cbdcc31?w=200', TRUE)
ON CONFLICT (nombre) DO UPDATE SET 
    descripcion = EXCLUDED.descripcion,
    icono = EXCLUDED.icono,
    activo = TRUE;

-- ============================================================
-- 2. PROFESIONES (25 Rutas Profesionales)
-- ============================================================
INSERT INTO profesiones (nombre, descripcion, activo) VALUES
('Frontend Developer', 'Especialista en interfaces de usuario web con React, Vue y CSS moderno', TRUE),
('Android Native Developer', 'Desarrollador experto en aplicaciones nativas con Kotlin y Jetpack Compose', TRUE),
('iOS Developer', 'Desarrollador de aplicaciones nativas para el ecosistema Apple con Swift', TRUE),
('Backend Developer con Java / Spring', 'Construcción de arquitecturas empresariales seguras y APIs de alto rendimiento', TRUE),
('Backend Developer con Node.js', 'Desarrollo de microservicios ágiles, APIs REST y GraphQL con TypeScript', TRUE),
('Fullstack Web Developer', 'Dominio integral del stack tecnológico desde la base de datos hasta la UI', TRUE),
('Mobile Developer Multiplataforma', 'Creación de aplicaciones nativas para iOS y Android usando Flutter o React Native', TRUE),
('Data Analyst', 'Interpretación analítica de datos, tableros con Power BI y SQL avanzado', TRUE),
('Data Scientist & ML Engineer', 'Modelado matemático predictivo, redes neuronales y machine learning con Python', TRUE),
('Cloud & DevOps Engineer', 'Especialista en infraestructura cloud, automatización con Docker y Kubernetes', TRUE),
('Cybersecurity Analyst', 'Seguridad perimetral, pentesting ético y mitigación de vulnerabilidades', TRUE),
('QA Automation Engineer', 'Diseño y ejecución de suites de pruebas automáticas e integración continua', TRUE),
('UI/UX Product Designer', 'Investigación con usuarios, wireframes de alta fidelidad y prototipado Figma', TRUE),
('Database Administrator (DBA)', 'Administración, afinamiento, índices y alta disponibilidad en PostgreSQL/MySQL', TRUE),
('Software Architect', 'Definición de lineamientos de software, patrones arquitectónicos y escalabilidad', TRUE),
('AI Solutions Developer', 'Integración de modelos fundacionales (LLMs), RAG y asistentes de inteligencia artificial', TRUE),
('Scrum Master & Agile Coach', 'Facilitador de metodologías ágiles y dinamizador de equipos de ingeniería', TRUE),
('Product Manager Tech', 'Estrategia de producto, priorización de backlog y descubrimiento de valor de negocio', TRUE),
('Blockchain Developer', 'Creación de protocolos descentralizados y contratos inteligentes en Ethereum', TRUE),
('Game Developer', 'Desarrollo de mecánicas interactivas y videojuegos 2D/3D con Unity y C#', TRUE),
('Digital Growth Hacker', 'Adquisición de usuarios, embudos de conversión, analítica y experimentación', TRUE),
('IT Project Manager', 'Planificación integral, gestión de recursos y entrega puntual de proyectos IT', TRUE),
('Ingeniero de Redes y Telecomunicaciones', 'Configuración de infraestructuras de red seguras, firewalls y servidores', TRUE),
('English for Tech Specialist', 'Comunicación oral fluida y redacción técnica en inglés para equipos internacionales', TRUE),
('Desarrollador de IoT y Sistemas Embebidos', 'Automatización de hardware, sensores inteligentes y computación perimetral', TRUE)
ON CONFLICT (nombre) DO UPDATE SET 
    descripcion = EXCLUDED.descripcion,
    activo = TRUE;

-- ============================================================
-- 3. CURSOS (25 Cursos Principales Bien Repartidos)
-- ============================================================
INSERT INTO cursos (id_categoria, nombre, descripcion, icono, xp_requerido, xp_recompensa, activo)
SELECT c.id_categoria, v.curso, v.descripcion, v.icono, v.xp_req, v.xp_rec, TRUE
FROM (VALUES
    -- Desarrollo Móvil
    ('Desarrollo Móvil', 'Curso de Kotlin Básico', 'Aprende el lenguaje moderno y oficial para Android: variables, null safety y OOP', 'https://images.unsplash.com/photo-1607252650355-f7fd0460ccdb?w=200', 0, 420),
    ('Desarrollo Móvil', 'Curso de Desarrollo de Apps con Android y Kotlin', 'Domina Activities, Fragments, RecyclerViews, ViewBinding y ciclo de vida de Android', 'https://images.unsplash.com/photo-1512941937669-90a1b58e7e9c?w=200', 100, 480),
    ('Desarrollo Móvil', 'Curso de Jetpack Compose Moderno', 'Crea interfaces declarativas reactivas con Compose, Material 3 y estado en Android', 'https://images.unsplash.com/photo-1551650975-87deedd944c3?w=200', 250, 500),
    ('Desarrollo Móvil', 'Curso Básico de Flutter', 'Desarrolla aplicaciones móviles fluidas con Dart y el catálogo de widgets de Flutter', 'https://images.unsplash.com/photo-1526498460520-4c246339dccb?w=200', 0, 450),
    ('Desarrollo Móvil', 'Curso de Introducción a React Native', 'Construye aplicaciones móviles multiplataforma nativas usando JavaScript y React', 'https://images.unsplash.com/photo-1581291518633-83b4ebd1d83e?w=200', 50, 460),

    -- Desarrollo Web
    ('Desarrollo Web', 'Curso Definitivo de HTML y CSS', 'Fundamentos esenciales de maquetado web, cajas, tipografía y responsive design', 'https://images.unsplash.com/photo-1507238691740-187a5b1d37b8?w=200', 0, 350),
    ('Desarrollo Web', 'Curso Práctico de HTML y CSS', 'Aplica Flexbox y CSS Grid maquetando una landing page y dashboard real', 'https://images.unsplash.com/photo-1522542550221-31fd19575a2d?w=200', 50, 400),
    ('Desarrollo Web', 'Curso Básico de JavaScript', 'Fundamentos de lógica web: funciones, arreglos, manipulación del DOM y eventos', 'https://images.unsplash.com/photo-1579468118864-ddab3f910408?w=200', 0, 450),
    ('Desarrollo Web', 'Curso de React.js Básico', 'Arquitectura basada en componentes, hooks, estado y consumo de APIs REST', 'https://images.unsplash.com/photo-1633356122544-f134324a6cee?w=200', 150, 490),
    ('Desarrollo Web', 'Curso de Backend con Node.js y Express', 'Diseña servidores rápidos, APIs RESTful seguras y middlewares con Node.js', 'https://images.unsplash.com/photo-1627398242454-45a1465c2479?w=200', 100, 480),

    -- Programación
    ('Programación', 'Curso Gratis de Programación Básica', 'Conceptos universales: algoritmos, variables, condicionales y bucles desde cero', 'https://images.unsplash.com/photo-1515879218367-8466d910aaa4?w=200', 0, 300),
    ('Programación', 'Curso de Pensamiento Lógico', 'Estrategias y diagramas de flujo para resolver problemas complejos de computación', 'https://images.unsplash.com/photo-1509228468518-180dd4864904?w=200', 0, 320),
    ('Programación', 'Curso de Estructuras de Datos y Algoritmos', 'Listas enlazadas, pilas, colas, árboles binarios y análisis de complejidad Big-O', 'https://images.unsplash.com/photo-1504639725590-34d0984388bd?w=200', 100, 470),
    ('Programación', 'Curso Profesional de Git y GitHub', 'Control de versiones en equipo, ramas, merges, pull requests y flujos Gitflow', 'https://images.unsplash.com/photo-1618401471353-b98afee0b2eb?w=200', 0, 380),

    -- Base de Datos
    ('Base de Datos y Big Data', 'Curso de Bases de Datos Relacionales con PostgreSQL', 'Modelado entidad-relación, normalización, consultas DDL/DML, JOINs y transacciones', 'https://images.unsplash.com/photo-1544383835-bda2bc66a55d?w=200', 50, 450),
    ('Base de Datos y Big Data', 'Curso de Optimización y Consultas SQL Avanzadas', 'Planes de ejecución EXPLAIN, índices B-Tree, particionamiento y procedimientos', 'https://images.unsplash.com/photo-1558494949-ef010cbdcc31?w=200', 150, 480),

    -- Cloud Computing y DevOps
    ('Cloud Computing y DevOps', 'Curso de Docker y Contenedores', 'Virtualización ligera, Dockerfiles, creación de imágenes y Docker Compose', 'https://images.unsplash.com/photo-1605745341112-85968b19335b?w=200', 100, 460),
    ('Cloud Computing y DevOps', 'Curso de Fundamentos de AWS Cloud', 'Servicios esenciales: EC2, S3, RDS, IAM, VPC y despliegue en la nube de Amazon', 'https://images.unsplash.com/photo-1451187580459-43490279c0fa?w=200', 150, 490),

    -- AI Academy
    ('AI Academy', 'Curso de Fundamentos de Inteligencia Artificial', 'Historia de la IA, algoritmos de búsqueda, conceptos de machine learning y deep learning', 'https://images.unsplash.com/photo-1677442136019-21780efad99a?w=200', 50, 460),
    ('AI Academy', 'Curso Práctico de Prompt Engineering y LLMs', 'Técnicas avanzadas de prompting, cadenas de pensamiento y uso de APIs de OpenAI y Gemini', 'https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?w=200', 80, 470),

    -- Ciberseguridad
    ('Ciberseguridad', 'Curso de Fundamentos de Seguridad Informática', 'Principios de confidencialidad, integridad, criptografía simétrica/asimétrica y OWASP Top 10', 'https://images.unsplash.com/photo-1563986768609-322da13575f3?w=200', 50, 440),

    -- Diseño de Producto y UX
    ('Diseño de Producto y UX', 'Curso de Figma para Desarrolladores', 'Diseño de sistemas de componentes, autolayout, variantes y prototipos interactivos', 'https://images.unsplash.com/photo-1581291518857-4e27b48ff24e?w=200', 0, 410),

    -- English Academy
    ('English Academy', 'Curso de Inglés Técnico para Desarrolladores', 'Terminología técnica de código, documentación de software y entrevistas laborales en inglés', 'https://images.unsplash.com/photo-1546410531-bb4caa6b424d?w=200', 0, 420),

    -- Calidad y Testing
    ('Calidad y Testing (QA)', 'Curso de Pruebas Unitarias y TDD', 'Metodología Test-Driven Development, mocks, aserciones y cobertura con JUnit', 'https://images.unsplash.com/photo-1516321318423-f06f85e504b3?w=200', 100, 450),

    -- Arquitectura de Software
    ('Arquitectura de Software', 'Curso de Patrones de Diseño de Software', 'Patrones Creacionales, Estructurales y de Comportamiento: Singleton, Factory, Observer', 'https://images.unsplash.com/photo-1504384308090-c894fdcc538d?w=200', 120, 490)
) AS v(categoria, curso, descripcion, icono, xp_req, xp_rec)
JOIN categorias c ON c.nombre = v.categoria
ON CONFLICT (id_categoria, nombre) DO UPDATE SET
    descripcion = EXCLUDED.descripcion,
    icono = EXCLUDED.icono,
    xp_requerido = EXCLUDED.xp_requerido,
    xp_recompensa = EXCLUDED.xp_recompensa,
    activo = TRUE;

-- ============================================================
-- 4. NIVELES (3 niveles para todos los cursos principales)
-- ============================================================
INSERT INTO niveles (id_curso, nombre, descripcion, orden)
SELECT c.id_curso, n.nombre, n.descripcion, n.orden
FROM cursos c
CROSS JOIN (VALUES 
    ('Básico', 'Fundamentos esenciales y conceptos clave para iniciarse en la materia.', 1), 
    ('Intermedio', 'Aplicación técnica de herramientas, resolución estructurada y prácticas guiadas.', 2), 
    ('Avanzado', 'Optimización, arquitectura profesional, buenas prácticas y proyectos complejos.', 3)
) AS n(nombre, descripcion, orden)
ON CONFLICT (id_curso, orden) DO UPDATE SET
    nombre = EXCLUDED.nombre,
    descripcion = EXCLUDED.descripcion;

-- ============================================================
-- 5. LECCIONES (25+ lecciones con contenido real educativo)
-- ============================================================
-- 5.1. Curso de Kotlin Básico (Nivel 1 - Básico)
INSERT INTO lecciones (id_nivel, titulo, descripcion, contenido, orden, xp_recompensa)
SELECT n.id_nivel, v.titulo, v.descripcion, v.contenido, v.orden, 25
FROM cursos c
JOIN niveles n ON n.id_curso = c.id_curso AND n.orden = 1
JOIN (VALUES
    (1, 'Introducción a Kotlin y la JVM', 'Historia, ventajas frente a Java y entorno de ejecución.', 'Kotlin es un lenguaje moderno, conciso y seguro desarrollado por JetBrains que compila a bytecode de la JVM. Es totalmente interoperable con Java y es el lenguaje preferido por Google para el desarrollo en Android. Sus principales beneficios radican en la reducción de código boilerplate y la seguridad frente a punteros nulos.'),
    (2, 'Variables: val vs var', 'Diferencia entre inmutabilidad y mutabilidad en Kotlin.', 'En Kotlin disponemos de dos palabras clave para declarar variables: "val" crea referencias de solo lectura (inmutables), mientras que "var" define variables mutables cuyo valor puede cambiar. La buena práctica en Kotlin recomienda priorizar siempre "val" a menos que la mutabilidad sea estrictamente requerida.'),
    (3, 'Tipos de datos básicos e inferencia', 'Enteros, flotantes, booleanos y cadenas de texto.', 'Kotlin cuenta con inferencia de tipos estática. Tipos primarios como Int, Double, Float, Boolean, Char y String son tratados como objetos sin sacrificar el rendimiento de tipos primitivos en tiempo de ejecución. Permite templates de texto con el símbolo $, por ejemplo: "Hola, $nombre".'),
    (4, 'Control de flujo: if y when', 'Expresiones condicionales modernas.', 'A diferencia de lenguajes tradicionales, "if" en Kotlin es una expresión que retorna un valor, eliminando la necesidad de operadores ternarios. La estructura "when" sustituye y supera al switch tradicional, permitiendo evaluar rangos, tipos y múltiples condiciones de forma muy legible.'),
    (5, 'Null Safety en Kotlin', 'El operador Elvis ?: y llamadas seguras ?.', 'El sistema de tipos de Kotlin distingue entre referencias que pueden ser nulas y las que no pueden serlo. Por defecto una variable no admite null (String). Si deseamos permitir nulos añadimos ? (String?). Para acceder de forma segura usamos ?. y para asignar un valor por defecto usamos el operador Elvis ?:.')
) AS v(orden, titulo, descripcion, contenido) ON TRUE
WHERE c.nombre = 'Curso de Kotlin Básico'
ON CONFLICT (id_nivel, orden) DO UPDATE SET
    titulo = EXCLUDED.titulo,
    descripcion = EXCLUDED.descripcion,
    contenido = EXCLUDED.contenido,
    xp_recompensa = EXCLUDED.xp_recompensa;

-- 5.2. Curso de Desarrollo de Apps con Android y Kotlin (Nivel 1 - Básico)
INSERT INTO lecciones (id_nivel, titulo, descripcion, contenido, orden, xp_recompensa)
SELECT n.id_nivel, v.titulo, v.descripcion, v.contenido, v.orden, 30
FROM cursos c
JOIN niveles n ON n.id_curso = c.id_curso AND n.orden = 1
JOIN (VALUES
    (1, 'Estructura de un Proyecto Android', 'Entendiendo AndroidManifest.xml, res y Gradle.', 'Un proyecto Android en Android Studio se compone de módulos. El archivo AndroidManifest.xml declara componentes vitales como actividades, permisos del sistema y servicios. La carpeta res aloja layouts XML, strings, drawables y temas visuales, mientras que Gradle gestiona dependencias y empaquetado del APK/AAB.'),
    (2, 'Ciclo de Vida de una Activity', 'onCreate, onStart, onResume, onPause, onStop, onDestroy.', 'Toda Activity pasa por diferentes estados según la interacción del usuario. onCreate inicializa la vista; onStart hace visible la pantalla; onResume permite la interacción. Cuando entra una llamada o el usuario sale, se activan onPause y onStop. Si el sistema necesita memoria o se cierra la pantalla, se invoca onDestroy.'),
    (3, 'ViewBinding: acceso seguro a vistas', 'Reemplazando findViewById de forma eficiente.', 'ViewBinding genera una clase de enlace para cada archivo XML de layout. Garantiza seguridad de tipos (type safety) y seguridad contra nulos (null safety), evitando las clásicas excepciones NullPointerException causadas por IDs erróneos en findViewById.'),
    (4, 'RecyclerView y Adapters', 'Listas dinámicas y eficientes en Android.', 'RecyclerView recicla vistas en memoria para optimizar el consumo de recursos al mostrar grandes listas. Requiere un Adapter que vincula el conjunto de datos con el ViewHolder, el cual almacena las referencias a los elementos visuales de cada item de la lista.'),
    (5, 'Navegación con Fragments y Jetpack Navigation', 'Arquitectura Single-Activity moderna.', 'Un Fragment representa una porción modular y reutilizable de la interfaz de usuario. Utilizando el componente Jetpack Navigation y un Navigation Graph, gestionamos el intercambio de fragmentos, animaciones de transición y paso de argumentos seguros mediante SafeArgs.')
) AS v(orden, titulo, descripcion, contenido) ON TRUE
WHERE c.nombre = 'Curso de Desarrollo de Apps con Android y Kotlin'
ON CONFLICT (id_nivel, orden) DO UPDATE SET
    titulo = EXCLUDED.titulo,
    descripcion = EXCLUDED.descripcion,
    contenido = EXCLUDED.contenido,
    xp_recompensa = EXCLUDED.xp_recompensa;

-- 5.3. Curso Básico de JavaScript (Nivel 1 - Básico)
INSERT INTO lecciones (id_nivel, titulo, descripcion, contenido, orden, xp_recompensa)
SELECT n.id_nivel, v.titulo, v.descripcion, v.contenido, v.orden, 25
FROM cursos c
JOIN niveles n ON n.id_curso = c.id_curso AND n.orden = 1
JOIN (VALUES
    (1, 'Variables y Alcance: var, let y const', 'Hoisting y scope de bloque en JavaScript.', 'ECMAScript 6 introdujo let y const para mitigar los problemas de alcance global y función de var. let y const poseen scope de bloque {} y no son accesibles antes de su declaración (zona muerta temporal TDZ), haciendo el código mucho más predecible.'),
    (2, 'Funciones Flecha y Parámetros por Defecto', 'Sintaxis compacta y manejo del contexto this.', 'Las arrow functions () => {} ofrecen una forma concisa de escribir funciones y no tienen su propio binding de this, sino que lo heredan léxicamente del ámbito circundante, lo cual es ideal para callbacks y métodos de arreglos.'),
    (3, 'Manipulación del DOM', 'Selección de nodos, eventos y modificación dinámica.', 'El Document Object Model (DOM) es la representación en memoria del árbol HTML. Con métodos modernos como querySelector y addEventListener podemos escuchar clics del usuario, modificar estilos, alternar clases CSS e inyectar nuevo contenido en tiempo real.'),
    (4, 'Arreglos y Métodos de Transformación', 'map, filter, reduce y forEach.', 'Los métodos funcionales de arrays permiten transformar colecciones sin mutar el arreglo original. map aplica una transformación y devuelve un nuevo array, filter extrae elementos que cumplen una condición booleana y reduce acumula valores en un único resultado.'),
    (5, 'Asincronía: Promesas y Async/Await', 'Consumo de APIs con fetch y manejo de errores.', 'JavaScript es monohilo y no bloqueante gracias al Event Loop. Las Promesas gestionan operaciones asíncronas pendientes, resueltas o rechazadas. La sintaxis async/await permite escribir código asíncrono con la claridad y fluidez del código síncrono tradicional, manejando fallas mediante bloques try/catch.')
) AS v(orden, titulo, descripcion, contenido) ON TRUE
WHERE c.nombre = 'Curso Básico de JavaScript'
ON CONFLICT (id_nivel, orden) DO UPDATE SET
    titulo = EXCLUDED.titulo,
    descripcion = EXCLUDED.descripcion,
    contenido = EXCLUDED.contenido,
    xp_recompensa = EXCLUDED.xp_recompensa;

-- 5.4. Curso de React.js Básico (Nivel 1 - Básico)
INSERT INTO lecciones (id_nivel, titulo, descripcion, contenido, orden, xp_recompensa)
SELECT n.id_nivel, v.titulo, v.descripcion, v.contenido, v.orden, 30
FROM cursos c
JOIN niveles n ON n.id_curso = c.id_curso AND n.orden = 1
JOIN (VALUES
    (1, 'Pensando en Componentes y JSX', 'Sintaxis declarativa y reutilización de elementos.', 'React basa su arquitectura en componentes funcionales que retornan JSX, una extensión de sintaxis que permite escribir código similar a HTML directamente dentro de JavaScript. Cada componente encapsula su propia estructura y comportamiento.'),
    (2, 'Props: Comunicación entre Componentes', 'Flujo unidireccional de datos.', 'Las props son entradas de solo lectura que un componente padre pasa a sus hijos. El flujo de datos en React es estrictamente descendente, lo que facilita el rastreo de errores y desacopla la lógica de presentación.'),
    (3, 'Estado con el Hook useState', 'Interactividad y renderizado reactivo.', 'El hook useState permite a los componentes funcionales mantener memoria interna. Cuando se invoca la función actualizadora del estado, React programa un re-renderizado eficiente de la interfaz utilizando el Virtual DOM.'),
    (4, 'Efectos Secundarios con useEffect', 'Llamadas a APIs, suscripciones y ciclo de vida.', 'El hook useEffect sincroniza un componente con sistemas externos, como consumir datos de un backend o escuchar eventos de ventana. Mediante el arreglo de dependencias se controla exactamente cuándo debe dispararse o limpiarse el efecto.'),
    (5, 'Manejo de Formularios y Eventos', 'Componentes controlados vs no controlados.', 'En un componente controlado de React, el estado del input es gobernado completamente por el estado de React mediante value y onChange, permitiendo validaciones en vivo, formateo automático y bloqueo de envíos incorrectos.')
) AS v(orden, titulo, descripcion, contenido) ON TRUE
WHERE c.nombre = 'Curso de React.js Básico'
ON CONFLICT (id_nivel, orden) DO UPDATE SET
    titulo = EXCLUDED.titulo,
    descripcion = EXCLUDED.descripcion,
    contenido = EXCLUDED.contenido,
    xp_recompensa = EXCLUDED.xp_recompensa;

-- 5.5. Curso de Bases de Datos Relacionales con PostgreSQL (Nivel 1 - Básico)
INSERT INTO lecciones (id_nivel, titulo, descripcion, contenido, orden, xp_recompensa)
SELECT n.id_nivel, v.titulo, v.descripcion, v.contenido, v.orden, 30
FROM cursos c
JOIN niveles n ON n.id_curso = c.id_curso AND n.orden = 1
JOIN (VALUES
    (1, 'Modelo Relacional y Diseño de Tablas', 'Claves primarias, foráneas y normalización.', 'El modelo relacional organiza datos en filas y columnas llamadas tablas. Una clave primaria (PRIMARY KEY) identifica de forma unívoca a cada registro, mientras que una clave foránea (FOREIGN KEY) establece integridad referencial entre entidades relacionadas.'),
    (2, 'Consultas Básicas: SELECT, WHERE y ORDER BY', 'Filtrado y ordenamiento de registros.', 'La sentencia SELECT recupera atributos específicos de una tabla. La cláusula WHERE filtra las filas basándose en comparaciones lógicas (=, !=, LIKE, IN, BETWEEN), y ORDER BY organiza la salida de forma ascendente (ASC) o descendente (DESC).'),
    (3, 'Relaciones y JOINs: INNER, LEFT y RIGHT', 'Uniendo tablas para consultas enriquecidas.', 'Los JOINs permiten combinar datos de dos o más tablas basándose en una columna en común. INNER JOIN devuelve solo filas que coinciden en ambas tablas, mientras que LEFT JOIN conserva todas las filas de la tabla izquierda aun si no existen coincidencias en la derecha.'),
    (4, 'Funciones de Agregación y GROUP BY', 'COUNT, SUM, AVG, MIN, MAX y HAVING.', 'Las funciones de agregación operan sobre múltiples filas para devolver un único valor consolidado. Utilizando GROUP BY podemos agrupar filas por categorías específicas, y con HAVING filtramos los grupos generados después de aplicar la agregación.'),
    (5, 'Transacciones ACID y Bloqueos', 'BEGIN, COMMIT y ROLLBACK para consistencia.', 'Una transacción garantiza las propiedades ACID (Atomicidad, Consistencia, Aislamiento y Durabilidad). O todas las operaciones dentro del bloque se aplican exitosamente con COMMIT, o ante cualquier error se revierten por completo con ROLLBACK.')
) AS v(orden, titulo, descripcion, contenido) ON TRUE
WHERE c.nombre = 'Curso de Bases de Datos Relacionales con PostgreSQL'
ON CONFLICT (id_nivel, orden) DO UPDATE SET
    titulo = EXCLUDED.titulo,
    descripcion = EXCLUDED.descripcion,
    contenido = EXCLUDED.contenido,
    xp_recompensa = EXCLUDED.xp_recompensa;

-- ============================================================
-- 6. RECURSOS DE LECCIÓN (25 Recursos Educativos)
-- ============================================================
INSERT INTO recursos_leccion (id_leccion, tipo, titulo, url, contenido, plataforma, duracion_segundos, orden, activo)
SELECT l.id_leccion, v.tipo, v.titulo, v.url, v.contenido, v.plataforma, v.duracion, v.orden_rec, TRUE
FROM (VALUES
    -- Recursos Kotlin Lección 1
    ('Curso de Kotlin Básico', 'Introducción a Kotlin y la JVM', 'VIDEO', 'Aprende Kotlin desde Cero', 'https://www.youtube.com/watch?v=sBji_n9_O2c', NULL, 'YOUTUBE', 600, 1),
    ('Curso de Kotlin Básico', 'Introducción a Kotlin y la JVM', 'TEXTO', 'Resumen Teórico: Por qué Kotlin', NULL, 'Kotlin reduce el código de Java en hasta un 40%, evitando clases contenedoras y código repetitivo. Es 100% interoperable con librerías Java existentes.', NULL, NULL, 2),
    ('Curso de Kotlin Básico', 'Introducción a Kotlin y la JVM', 'ENLACE', 'Documentación Oficial de Kotlin', 'https://kotlinlang.org/docs/home.html', NULL, NULL, NULL, 3),

    -- Recursos Kotlin Lección 2
    ('Curso de Kotlin Básico', 'Variables: val vs var', 'VIDEO', 'Mutabilidad e Inmutabilidad en Kotlin', 'https://www.youtube.com/watch?v=F9UC9DY-vIU', NULL, 'YOUTUBE', 420, 1),
    ('Curso de Kotlin Básico', 'Variables: val vs var', 'TEXTO', 'Regla de Oro de Variables', NULL, 'Prioriza siempre val. Cambia a var únicamente si necesitas reasignar el valor más adelante dentro del flujo.', NULL, NULL, 2),

    -- Recursos Android Lección 1
    ('Curso de Desarrollo de Apps con Android y Kotlin', 'Estructura de un Proyecto Android', 'VIDEO', 'Tour por Android Studio y Gradle', 'https://www.youtube.com/watch?v=EExSSotojVI', NULL, 'YOUTUBE', 720, 1),
    ('Curso de Desarrollo de Apps con Android y Kotlin', 'Estructura de un Proyecto Android', 'TEXTO', 'Archivos Clave en Android', NULL, 'El AndroidManifest define permisos y Activities. En res/values/strings.xml se guardan los textos para soporte multiidioma.', NULL, NULL, 2),
    ('Curso de Desarrollo de Apps con Android y Kotlin', 'Estructura de un Proyecto Android', 'ENLACE', 'Guía Oficial de Android Developers', 'https://developer.android.com/guide', NULL, NULL, 3),

    -- Recursos Android Lección 2
    ('Curso de Desarrollo de Apps con Android y Kotlin', 'Ciclo de Vida de una Activity', 'VIDEO', 'Ciclo de Vida Explicado con Ejemplos', 'https://www.youtube.com/watch?v=2TzF3bVbCgM', NULL, 'YOUTUBE', 540, 1),
    ('Curso de Desarrollo de Apps con Android y Kotlin', 'Ciclo de Vida de una Activity', 'TEXTO', 'Diagrama Mental del Ciclo de Vida', NULL, 'onCreate -> onStart -> onResume (Usuario interactúa) -> onPause -> onStop -> onDestroy.', NULL, NULL, 2),

    -- Recursos Android Lección 3
    ('Curso de Desarrollo de Apps con Android y Kotlin', 'ViewBinding: acceso seguro a vistas', 'VIDEO', 'Cómo usar ViewBinding paso a paso', 'https://www.youtube.com/watch?v=wXbX8zN_80E', NULL, 'YOUTUBE', 480, 1),
    ('Curso de Desarrollo de Apps con Android y Kotlin', 'ViewBinding: acceso seguro a vistas', 'TEXTO', 'Configuración de build.gradle', NULL, 'Activa viewBinding dentro del bloque buildFeatures { viewBinding = true } en tu archivo build.gradle.kts.', NULL, NULL, 2),

    -- Recursos JavaScript Lección 1
    ('Curso Básico de JavaScript', 'Variables y Alcance: var, let y const', 'VIDEO', 'Scope y Hoisting en JS', 'https://www.youtube.com/watch?v=b0Yn4fE7g3Y', NULL, 'YOUTUBE', 510, 1),
    ('Curso Básico de JavaScript', 'Variables y Alcance: var, let y const', 'TEXTO', 'Diferencias Rápidas de Scope', NULL, 'var tiene scope de función. let y const tienen scope de bloque y no permiten redeclaración en el mismo bloque.', NULL, NULL, 2),

    -- Recursos JavaScript Lección 4
    ('Curso Básico de JavaScript', 'Arreglos y Métodos de Transformación', 'VIDEO', 'Aprende map, filter y reduce', 'https://www.youtube.com/watch?v=tPEE99b4d8s', NULL, 'YOUTUBE', 620, 1),
    ('Curso Básico de JavaScript', 'Arreglos y Métodos de Transformación', 'ENLACE', 'MDN Web Docs: Array Methods', 'https://developer.mozilla.org/es/docs/Web/JavaScript/Reference/Global_Objects/Array', NULL, NULL, 2),

    -- Recursos React Lección 1
    ('Curso de React.js Básico', 'Pensando en Componentes y JSX', 'VIDEO', 'Introducción a React y JSX', 'https://www.youtube.com/watch?v=MPLN1ahXdUo', NULL, 'YOUTUBE', 800, 1),
    ('Curso de React.js Básico', 'Pensando en Componentes y JSX', 'TEXTO', 'Reglas de JSX', NULL, 'Todo elemento debe cerrarse (<img />), las clases CSS se definen con className y las expresiones JS van entre llaves {}.', NULL, NULL, 2),

    -- Recursos React Lección 3
    ('Curso de React.js Básico', 'Estado con el Hook useState', 'VIDEO', 'Dominando useState en React', 'https://www.youtube.com/watch?v=4ORZ1GmdaMc', NULL, 'YOUTUBE', 580, 1),
    ('Curso de React.js Básico', 'Estado con el Hook useState', 'TEXTO', 'Inmutabilidad del Estado', NULL, 'Nunca modifiques el estado directamente (estado = nuevo). Usa siempre la función actualizadora setEstado.', NULL, NULL, 2),

    -- Recursos PostgreSQL Lección 1
    ('Curso de Bases de Datos Relacionales con PostgreSQL', 'Modelo Relacional y Diseño de Tablas', 'VIDEO', 'Introducción a Bases de Datos y SQL', 'https://www.youtube.com/watch?v=DFg1V-rO6Pg', NULL, 'YOUTUBE', 750, 1),
    ('Curso de Bases de Datos Relacionales con PostgreSQL', 'Modelo Relacional y Diseño de Tablas', 'TEXTO', 'Claves y Restricciones', NULL, 'Una FOREIGN KEY asegura que los registros vinculados existan en la tabla padre, evitando registros huérfanos.', NULL, NULL, 2),

    -- Recursos PostgreSQL Lección 3
    ('Curso de Bases de Datos Relacionales con PostgreSQL', 'Relaciones y JOINs: INNER, LEFT y RIGHT', 'VIDEO', 'Visualizando JOINs en SQL', 'https://www.youtube.com/watch?v=0h2b3c_9w2M', NULL, 'YOUTUBE', 530, 1),
    ('Curso de Bases de Datos Relacionales con PostgreSQL', 'Relaciones y JOINs: INNER, LEFT y RIGHT', 'TEXTO', 'Cuándo usar cada JOIN', NULL, 'Usa INNER JOIN cuando ambos lados sean obligatorios. Usa LEFT JOIN cuando quieras mantener todos los registros de la primera tabla.', NULL, NULL, 2),
    ('Curso de Bases de Datos Relacionales con PostgreSQL', 'Relaciones y JOINs: INNER, LEFT y RIGHT', 'ENLACE', 'PostgreSQL Tutorial: Queries & Joins', 'https://www.postgresqltutorial.com', NULL, NULL, 3)
) AS v(curso, leccion, tipo, titulo, url, contenido, plataforma, duracion, orden_rec)
JOIN cursos c ON c.nombre = v.curso
JOIN niveles n ON n.id_curso = c.id_curso
JOIN lecciones l ON l.id_nivel = n.id_nivel AND l.titulo = v.leccion
ON CONFLICT (id_leccion, orden) DO UPDATE SET
    tipo = EXCLUDED.tipo,
    titulo = EXCLUDED.titulo,
    url = EXCLUDED.url,
    contenido = EXCLUDED.contenido,
    plataforma = EXCLUDED.plataforma,
    duracion_segundos = EXCLUDED.duracion_segundos,
    activo = TRUE;

-- ============================================================
-- 7. EJERCICIOS (25 Ejercicios Interactivos)
-- ============================================================
INSERT INTO ejercicios (id_leccion, tipo_ejercicio, enunciado, orden, explicacion, xp_recompensa)
SELECT l.id_leccion, v.tipo, v.enunciado, v.orden_ej, v.explicacion, 20
FROM (VALUES
    -- Kotlin
    ('Curso de Kotlin Básico', 'Variables: val vs var', 'OPCION_MULTIPLE', '¿Cuál es la principal diferencia entre declarar una variable con val y una con var en Kotlin?', 1, 'val declara una referencia inmutable (solo lectura), mientras que var declara una variable mutable.'),
    ('Curso de Kotlin Básico', 'Null Safety en Kotlin', 'OPCION_MULTIPLE', '¿Qué operador se utiliza en Kotlin para proporcionar un valor alternativo en caso de que una expresión sea nula (Operador Elvis)?', 1, 'El operador Elvis ?: evalúa la expresión izquierda; si no es nula la retorna, de lo contrario devuelve el valor derecho.'),
    ('Curso de Kotlin Básico', 'Control de flujo: if y when', 'OPCION_MULTIPLE', '¿Qué característica especial tiene la estructura if en Kotlin frente a Java tradicional?', 1, 'En Kotlin if es una expresión que retorna un valor computable.'),

    -- Android
    ('Curso de Desarrollo de Apps con Android y Kotlin', 'Ciclo de Vida de una Activity', 'OPCION_MULTIPLE', '¿Cuál es el método del ciclo de vida en el que la Activity se vuelve visible e interactiva para el usuario?', 1, 'onResume() es el estado en que la actividad está en primer plano y el usuario puede interactuar con ella.'),
    ('Curso de Desarrollo de Apps con Android y Kotlin', 'ViewBinding: acceso seguro a vistas', 'OPCION_MULTIPLE', '¿Qué beneficio crucial proporciona ViewBinding en comparación con findViewById?', 1, 'Garantiza seguridad de tipos y contra nulos en tiempo de compilación.'),
    ('Curso de Desarrollo de Apps con Android y Kotlin', 'RecyclerView y Adapters', 'OPCION_MULTIPLE', '¿Cuál es la función del ViewHolder en un RecyclerView?', 1, 'Almacenar y reciclar las referencias a las vistas de cada elemento para evitar llamadas repetitivas a findViewById.'),
    ('Curso de Desarrollo de Apps con Android y Kotlin', 'Navegación con Fragments y Jetpack Navigation', 'OPCION_MULTIPLE', '¿Qué plugin de Gradle permite pasar argumentos de forma tipada y segura entre fragmentos?', 1, 'SafeArgs genera clases de argumentos seguras para el Navigation Component.'),

    -- JavaScript
    ('Curso Básico de JavaScript', 'Variables y Alcance: var, let y const', 'OPCION_MULTIPLE', '¿Qué sucede si intentas reasignar el valor de una variable declarada con const?', 1, 'Lanza un error de tipo TypeError indicando que no se puede reasignar una constante.'),
    ('Curso Básico de JavaScript', 'Manipulación del DOM', 'OPCION_MULTIPLE', '¿Qué método moderno permite seleccionar el primer elemento del DOM que coincida con un selector CSS?', 1, 'document.querySelector() retorna el primer nodo que coincide con el selector especificado.'),
    ('Curso Básico de JavaScript', 'Arreglos y Métodos de Transformación', 'OPCION_MULTIPLE', '¿Qué método de array devuelve un NUEVO arreglo con los elementos que cumplan una condición dada?', 1, 'filter() evalúa una función predicado y devuelve una nueva lista con los elementos coincidentes.'),
    ('Curso Básico de JavaScript', 'Asincronía: Promesas y Async/Await', 'OPCION_MULTIPLE', '¿Qué palabra clave debe colocarse antes de una función para poder usar await en su interior?', 1, 'La palabra clave async es obligatoria para habilitar el uso de await.'),

    -- React
    ('Curso de React.js Básico', 'Pensando en Componentes y JSX', 'OPCION_MULTIPLE', '¿Por qué en JSX se usa className en lugar de class para asignar clases CSS?', 1, 'Porque class es una palabra reservada en JavaScript.'),
    ('Curso de React.js Básico', 'Props: Comunicación entre Componentes', 'OPCION_MULTIPLE', '¿Cuál es la dirección del flujo de datos por defecto en una aplicación React?', 1, 'El flujo es unidireccional y descendente: de padres a hijos.'),
    ('Curso de React.js Básico', 'Estado con el Hook useState', 'OPCION_MULTIPLE', '¿Qué retorna la invocación del hook useState en React?', 1, 'Retorna un arreglo con exactamente dos elementos: el valor actual del estado y la función para actualizarlo.'),
    ('Curso de React.js Básico', 'Efectos Secundarios con useEffect', 'OPCION_MULTIPLE', '¿Cuándo se ejecuta un useEffect cuyo arreglo de dependencias está vacío []?', 1, 'Se ejecuta únicamente una vez, cuando el componente se monta por primera vez.'),

    -- PostgreSQL
    ('Curso de Bases de Datos Relacionales con PostgreSQL', 'Modelo Relacional y Diseño de Tablas', 'OPCION_MULTIPLE', '¿Qué restricción de base de datos asegura que todos los valores de una columna sean no repetidos y no nulos?', 1, 'La restricción PRIMARY KEY garantiza unicidad y obligatoriedad de datos.'),
    ('Curso de Bases de Datos Relacionales con PostgreSQL', 'Consultas Básicas: SELECT, WHERE y ORDER BY', 'OPCION_MULTIPLE', '¿Qué comando SQL se utiliza para filtrar resultados basándose en un patrón de texto con comodines?', 1, 'El operador LIKE (o ILIKE en PostgreSQL) permite filtrar con comodines como %.'),
    ('Curso de Bases de Datos Relacionales con PostgreSQL', 'Relaciones y JOINs: INNER, LEFT y RIGHT', 'OPCION_MULTIPLE', '¿Qué tipo de JOIN devuelve todas las filas de la tabla izquierda y las coincidentes de la derecha?', 1, 'LEFT JOIN conserva todos los registros de la primera tabla.'),
    ('Curso de Bases de Datos Relacionales con PostgreSQL', 'Funciones de Agregación y GROUP BY', 'OPCION_MULTIPLE', '¿Qué cláusula se debe utilizar para filtrar los resultados de una función de agregación después de un GROUP BY?', 1, 'La cláusula HAVING se emplea para filtrar condiciones sobre grupos agregados.'),
    ('Curso de Bases de Datos Relacionales con PostgreSQL', 'Transacciones ACID y Bloqueos', 'OPCION_MULTIPLE', '¿Qué comando descarta todos los cambios realizados durante la transacción actual en PostgreSQL?', 1, 'ROLLBACK cancela todas las modificaciones pendientes de la transacción.'),

    -- HTML y CSS
    ('Curso Definitivo de HTML y CSS', 'Introducción a la Web', 'OPCION_MULTIPLE', '¿Qué código de estado HTTP indica una respuesta exitosa del servidor web?', 2, 'El código 200 OK indica que la solicitud fue procesada con éxito.'),
    ('Curso Definitivo de HTML y CSS', '¿Qué es HTML?', 'OPCION_MULTIPLE', '¿Qué etiqueta envuelve todos los metadatos no visibles de un documento HTML5?', 1, 'La etiqueta <head> aloja títulos, enlaces a estilos y metadatos.'),
    ('Curso Definitivo de HTML y CSS', 'Etiquetas de texto', 'OPCION_MULTIPLE', '¿Por qué es una buena práctica de SEO utilizar únicamente un encabezado <h1> por página?', 1, 'Porque el <h1> indica a los motores de búsqueda el tema principal y exclusivo del documento.'),
    ('Curso Definitivo de HTML y CSS', 'El Modelo de Caja', 'OPCION_MULTIPLE', '¿Qué propiedad de CSS define el espacio interior entre el contenido y el borde de un elemento?', 1, 'padding define el relleno interno del box model.'),
    ('Curso Definitivo de HTML y CSS', 'Introducción a Flexbox', 'OPCION_MULTIPLE', '¿Qué valor de la propiedad display activa el modelo de diseño flexible en un contenedor?', 1, 'display: flex activa el contexto de formato flexible.')
) AS v(curso, leccion, tipo, enunciado, orden_ej, explicacion)
JOIN cursos c ON c.nombre = v.curso
JOIN niveles n ON n.id_curso = c.id_curso
JOIN lecciones l ON l.id_nivel = n.id_nivel AND l.titulo = v.leccion
ON CONFLICT (id_leccion, orden) DO UPDATE SET
    tipo_ejercicio = EXCLUDED.tipo_ejercicio,
    enunciado = EXCLUDED.enunciado,
    explicacion = EXCLUDED.explicacion,
    xp_recompensa = EXCLUDED.xp_recompensa;

-- ============================================================
-- 8. OPCIONES DE EJERCICIO (75 Opciones para los ejercicios)
-- ============================================================
INSERT INTO opciones_ejercicio (id_ejercicio, texto, es_correcta, orden)
SELECT e.id_ejercicio, v.texto, v.es_correcta, v.orden_op
FROM ejercicios e
JOIN lecciones l ON e.id_leccion = l.id_leccion
JOIN (VALUES
    -- Ejercicio: val vs var
    ('¿Cuál es la principal diferencia entre declarar una variable con val y una con var en Kotlin?', 'val es inmutable (solo lectura), mientras que var es mutable', TRUE, 1),
    ('¿Cuál es la principal diferencia entre declarar una variable con val y una con var en Kotlin?', 'val solo admite números enteros y var solo cadenas de texto', FALSE, 2),
    ('¿Cuál es la principal diferencia entre declarar una variable con val y una con var en Kotlin?', 'var se destruye automáticamente al terminar la función y val es global', FALSE, 3),

    -- Ejercicio: Null Safety
    ('¿Qué operador se utiliza en Kotlin para proporcionar un valor alternativo en caso de que una expresión sea nula (Operador Elvis)?', 'El operador ?: ', TRUE, 1),
    ('¿Qué operador se utiliza en Kotlin para proporcionar un valor alternativo en caso de que una expresión sea nula (Operador Elvis)?', 'El operador ?? ', FALSE, 2),
    ('¿Qué operador se utiliza en Kotlin para proporcionar un valor alternativo en caso de que una expresión sea nula (Operador Elvis)?', 'El operador || ', FALSE, 3),

    -- Ejercicio: if en Kotlin
    ('¿Qué característica especial tiene la estructura if en Kotlin frente a Java tradicional?', 'Es una expresión que retorna un valor directamente', TRUE, 1),
    ('¿Qué característica especial tiene la estructura if en Kotlin frente a Java tradicional?', 'Solo puede evaluar valores booleanos literales', FALSE, 2),
    ('¿Qué característica especial tiene la estructura if en Kotlin frente a Java tradicional?', 'Requiere siempre el uso de un bucle while asociado', FALSE, 3),

    -- Ejercicio: Ciclo de vida Activity
    ('¿Cuál es el método del ciclo de vida en el que la Activity se vuelve visible e interactiva para el usuario?', 'onResume()', TRUE, 1),
    ('¿Cuál es el método del ciclo de vida en el que la Activity se vuelve visible e interactiva para el usuario?', 'onStop()', FALSE, 2),
    ('¿Cuál es el método del ciclo de vida en el que la Activity se vuelve visible e interactiva para el usuario?', 'onDestroy()', FALSE, 3),

    -- Ejercicio: ViewBinding
    ('¿Qué beneficio crucial proporciona ViewBinding en comparación con findViewById?', 'Garantiza seguridad de tipos y previene NullPointerException en tiempo de compilación', TRUE, 1),
    ('¿Qué beneficio crucial proporciona ViewBinding en comparación con findViewById?', 'Aumenta automáticamente la velocidad del procesador móvil', FALSE, 2),
    ('¿Qué beneficio crucial proporciona ViewBinding en comparación con findViewById?', 'Elimina por completo la necesidad de escribir archivos de layout XML', FALSE, 3),

    -- Ejercicio: RecyclerView ViewHolder
    ('¿Cuál es la función del ViewHolder en un RecyclerView?', 'Guardar referencias a las vistas para reciclarlas al hacer scroll y evitar llamadas costosas', TRUE, 1),
    ('¿Cuál es la función del ViewHolder en un RecyclerView?', 'Descargar imágenes de internet en segundo plano de manera automática', FALSE, 2),
    ('¿Cuál es la función del ViewHolder en un RecyclerView?', 'Gestionar la base de datos SQLite interna de la aplicación', FALSE, 3),

    -- Ejercicio: Navigation SafeArgs
    ('¿Qué plugin de Gradle permite pasar argumentos de forma tipada y segura entre fragmentos?', 'SafeArgs', TRUE, 1),
    ('¿Qué plugin de Gradle permite pasar argumentos de forma tipada y segura entre fragmentos?', 'DataBinding Helper', FALSE, 2),
    ('¿Qué plugin de Gradle permite pasar argumentos de forma tipada y segura entre fragmentos?', 'Android Parcelable Generator', FALSE, 3),

    -- Ejercicio: JavaScript const
    ('¿Qué sucede si intentas reasignar el valor de una variable declarada con const?', 'Se produce una excepción de tipo TypeError en tiempo de ejecución', TRUE, 1),
    ('¿Qué sucede si intentas reasignar el valor de una variable declarada con const?', 'La variable pasa a ser de tipo var automáticamente', FALSE, 2),
    ('¿Qué sucede si intentas reasignar el valor de una variable declarada con const?', 'El navegador ignora la reasignación de forma silenciosa sin dar error', FALSE, 3),

    -- Ejercicio: JS DOM querySelector
    ('¿Qué método moderno permite seleccionar el primer elemento del DOM que coincida con un selector CSS?', 'document.querySelector()', TRUE, 1),
    ('¿Qué método moderno permite seleccionar el primer elemento del DOM que coincida con un selector CSS?', 'document.getElementByIdAll()', FALSE, 2),
    ('¿Qué método moderno permite seleccionar el primer elemento del DOM que coincida con un selector CSS?', 'document.findCssElement()', FALSE, 3),

    -- Ejercicio: JS Array filter
    ('¿Qué método de array devuelve un NUEVO arreglo con los elementos que cumplan una condición dada?', 'filter()', TRUE, 1),
    ('¿Qué método de array devuelve un NUEVO arreglo con los elementos que cumplan una condición dada?', 'forEach()', FALSE, 2),
    ('¿Qué método de array devuelve un NUEVO arreglo con los elementos que cumplan una condición dada?', 'push()', FALSE, 3),

    -- Ejercicio: JS async await
    ('¿Qué palabra clave debe colocarse antes de una función para poder usar await en su interior?', 'async', TRUE, 1),
    ('¿Qué palabra clave debe colocarse antes de una función para poder usar await en su interior?', 'defer', FALSE, 2),
    ('¿Qué palabra clave debe colocarse antes de una función para poder usar await en su interior?', 'promise', FALSE, 3),

    -- Ejercicio: React className
    ('¿Por qué en JSX se usa className en lugar de class para asignar clases CSS?', 'Porque class es una palabra reservada en la sintaxis de JavaScript', TRUE, 1),
    ('¿Por qué en JSX se usa className en lugar de class para asignar clases CSS?', 'Porque el navegador no reconoce class en ningún documento moderno', FALSE, 2),
    ('¿Por qué en JSX se usa className en lugar de class para asignar clases CSS?', 'Por compatibilidad exclusiva con Internet Explorer 6', FALSE, 3),

    -- Ejercicio: React flujo de datos
    ('¿Cuál es la dirección del flujo de datos por defecto en una aplicación React?', 'Unidireccional y descendente: desde los componentes padres hacia los hijos', TRUE, 1),
    ('¿Cuál es la dirección del flujo de datos por defecto en una aplicación React?', 'Bidireccional y caótico entre cualquier componente sin jerarquía', FALSE, 2),
    ('¿Cuál es la dirección del flujo de datos por defecto en una aplicación React?', 'Exclusivamente ascendente desde los hijos hacia los padres', FALSE, 3),

    -- Ejercicio: React useState
    ('¿Qué retorna la invocación del hook useState en React?', 'Una tupla/arreglo con el valor actual del estado y su función despachadora de cambio', TRUE, 1),
    ('¿Qué retorna la invocación del hook useState en React?', 'Únicamente una cadena de texto plana con el nombre de la variable', FALSE, 2),
    ('¿Qué retorna la invocación del hook useState en React?', 'Una promesa asíncrona que debe resolverse con .then()', FALSE, 3),

    -- Ejercicio: React useEffect []
    ('¿Cuándo se ejecuta un useEffect cuyo arreglo de dependencias está vacío []?', 'Se ejecuta exactamente una sola vez tras el primer montaje del componente', TRUE, 1),
    ('¿Cuándo se ejecuta un useEffect cuyo arreglo de dependencias está vacío []?', 'En cada milisegundo en un bucle infinito de render', FALSE, 2),
    ('¿Cuándo se ejecuta un useEffect cuyo arreglo de dependencias está vacío []?', 'Nunca se ejecuta', FALSE, 3),

    -- Ejercicio: SQL Primary Key
    ('¿Qué restricción de base de datos asegura que todos los valores de una columna sean no repetidos y no nulos?', 'PRIMARY KEY', TRUE, 1),
    ('¿Qué restricción de base de datos asegura que todos los valores de una columna sean no repetidos y no nulos?', 'FOREIGN KEY', FALSE, 2),
    ('¿Qué restricción de base de datos asegura que todos los valores de una columna sean no repetidos y no nulos?', 'CHECK DEFAULT', FALSE, 3),

    -- Ejercicio: SQL LIKE
    ('¿Qué comando SQL se utiliza para filtrar resultados basándose en un patrón de texto con comodines?', 'El operador LIKE (o ILIKE)', TRUE, 1),
    ('¿Qué comando SQL se utiliza para filtrar resultados basándose en un patrón de texto con comodines?', 'El operador EQUALS MATCH', FALSE, 2),
    ('¿Qué comando SQL se utiliza para filtrar resultados basándose en un patrón de texto con comodines?', 'El operador CONTAINS ALL', FALSE, 3),

    -- Ejercicio: SQL LEFT JOIN
    ('¿Qué tipo de JOIN devuelve todas las filas de la tabla izquierda y las coincidentes de la derecha?', 'LEFT JOIN', TRUE, 1),
    ('¿Qué tipo de JOIN devuelve todas las filas de la tabla izquierda y las coincidentes de la derecha?', 'CROSS JOIN', FALSE, 2),
    ('¿Qué tipo de JOIN devuelve todas las filas de la tabla izquierda y las coincidentes de la derecha?', 'INNER JOIN', FALSE, 3),

    -- Ejercicio: SQL HAVING
    ('¿Qué cláusula se debe utilizar para filtrar los resultados de una función de agregación después de un GROUP BY?', 'HAVING', TRUE, 1),
    ('¿Qué cláusula se debe utilizar para filtrar los resultados de una función de agregación después de un GROUP BY?', 'WHERE ONLY', FALSE, 2),
    ('¿Qué cláusula se debe utilizar para filtrar los resultados de una función de agregación después de un GROUP BY?', 'ORDER LIMIT', FALSE, 3),

    -- Ejercicio: SQL ROLLBACK
    ('¿Qué comando descarta todos los cambios realizados durante la transacción actual en PostgreSQL?', 'ROLLBACK', TRUE, 1),
    ('¿Qué comando descarta todos los cambios realizados durante la transacción actual en PostgreSQL?', 'COMMIT CANCEL', FALSE, 2),
    ('¿Qué comando descarta todos los cambios realizados durante la transacción actual en PostgreSQL?', 'RESET ALL TABLES', FALSE, 3),

    -- Ejercicio: HTTP 200
    ('¿Qué código de estado HTTP indica una respuesta exitosa del servidor web?', '200 OK', TRUE, 1),
    ('¿Qué código de estado HTTP indica una respuesta exitosa del servidor web?', '404 Not Found', FALSE, 2),
    ('¿Qué código de estado HTTP indica una respuesta exitosa del servidor web?', '500 Internal Error', FALSE, 3),

    -- Ejercicio: HTML head
    ('¿Qué etiqueta envuelve todos los metadatos no visibles de un documento HTML5?', '<head>', TRUE, 1),
    ('¿Qué etiqueta envuelve todos los metadatos no visibles de un documento HTML5?', '<body>', FALSE, 2),
    ('¿Qué etiqueta envuelve todos los metadatos no visibles de un documento HTML5?', '<footer>', FALSE, 3),

    -- Ejercicio: HTML h1
    ('¿Por qué es una buena práctica de SEO utilizar únicamente un encabezado <h1> por página?', 'Porque define el tema principal del documento para accesibilidad y buscadores', TRUE, 1),
    ('¿Por qué es una buena práctica de SEO utilizar únicamente un encabezado <h1> por página?', 'Porque el navegador bloquea la página si encuentra dos etiquetas h1', FALSE, 2),
    ('¿Por qué es una buena práctica de SEO utilizar únicamente un encabezado <h1> por página?', 'Porque el texto de los demás encabezados se borraría de la memoria', FALSE, 3),

    -- Ejercicio: CSS Box Model padding
    ('¿Qué propiedad de CSS define el espacio interior entre el contenido y el borde de un elemento?', 'padding', TRUE, 1),
    ('¿Qué propiedad de CSS define el espacio interior entre el contenido y el borde de un elemento?', 'margin', FALSE, 2),
    ('¿Qué propiedad de CSS define el espacio interior entre el contenido y el borde de un elemento?', 'outline', FALSE, 3),

    -- Ejercicio: CSS Flexbox display
    ('¿Qué valor de la propiedad display activa el modelo de diseño flexible en un contenedor?', 'display: flex', TRUE, 1),
    ('¿Qué valor de la propiedad display activa el modelo de diseño flexible en un contenedor?', 'display: grid-inline', FALSE, 2),
    ('¿Qué valor de la propiedad display activa el modelo de diseño flexible en un contenedor?', 'display: block-flex', FALSE, 3)
) AS v(enunciado, texto, es_correcta, orden_op)
ON e.enunciado = v.enunciado
ON CONFLICT (id_ejercicio, orden) DO UPDATE SET
    texto = EXCLUDED.texto,
    es_correcta = EXCLUDED.es_correcta;

-- ============================================================
-- 9. CATEGORÍAS DE PREGUNTA PARA DIAGNÓSTICO (12 Categorías)
-- ============================================================
INSERT INTO categorias_pregunta (nombre, descripcion) VALUES
('Fundamentos de Programación', 'Conceptos de variables, tipos, bucles y lógica booleana'),
('Programación Orientada a Objetos', 'Clases, encapsulamiento, polimorfismo y herencia'),
('Estructuras de Datos y Algoritmos', 'Complejidad algorítmica, arrays, árboles y listas'),
('Desarrollo Android y Móvil', 'Ecosistema Android, activities, ciclo de vida y Kotlin'),
('Desarrollo Web Frontend', 'HTML5 semántico, maquetación CSS y JavaScript moderno'),
('Desarrollo Web Backend', 'Servidores HTTP, APIs RESTful, Node.js y frameworks'),
('Bases de Datos Relacionales', 'Sentencias SQL, modelado de esquemas y normalización'),
('Cloud & DevOps', 'Contenedores Docker, CI/CD y despliegue en la nube'),
('Ciberseguridad y Hacking Ético', 'Protocolos criptográficos, vulnerabilidades y mitigación'),
('Arquitectura de Software', 'Clean Architecture, patrones SOLID y desacoplamiento'),
('Testing y Calidad de Código', 'Pruebas unitarias, TDD, mocks y aserciones'),
('Inglés Técnico para TI', 'Comprensión lectora de documentación y vocabulario técnico')
ON CONFLICT (nombre) DO UPDATE SET descripcion = EXCLUDED.descripcion;

-- ============================================================
-- 10. EXÁMENES DE DIAGNÓSTICO (15 Exámenes de Nivelación)
-- ============================================================
INSERT INTO examenes_diagnostico (id_profesion, id_curso, nombre, descripcion, activo)
SELECT p.id_profesion, NULL, 'Diagnóstico para ' || p.nombre, 'Evalúa tus conocimientos previos para la ruta de ' || p.nombre, TRUE
FROM profesiones p
WHERE p.nombre IN (
    'Android Native Developer',
    'Frontend Developer',
    'Backend Developer con Node.js',
    'Backend Developer con Java / Spring',
    'Mobile Developer Multiplataforma',
    'Fullstack Web Developer',
    'Data Analyst',
    'Cloud & DevOps Engineer',
    'Cybersecurity Analyst',
    'Database Administrator (DBA)',
    'UI/UX Product Designer',
    'QA Automation Engineer',
    'Software Architect',
    'AI Solutions Developer',
    'English for Tech Specialist'
)
ON CONFLICT DO NOTHING;

-- Exámenes ligados a cursos específicos
INSERT INTO examenes_diagnostico (id_profesion, id_curso, nombre, descripcion, activo)
SELECT NULL, c.id_curso, 'Evaluación Inicial: ' || c.nombre, 'Examen rápido para convalidar el nivel básico de ' || c.nombre, TRUE
FROM cursos c
WHERE c.nombre IN (
    'Curso de Kotlin Básico',
    'Curso Definitivo de HTML y CSS',
    'Curso Básico de JavaScript',
    'Curso de React.js Básico',
    'Curso de Bases de Datos Relacionales con PostgreSQL',
    'Curso Profesional de Git y GitHub',
    'Curso de Docker y Contenedores',
    'Curso de Fundamentos de Inteligencia Artificial',
    'Curso de Pruebas Unitarias y TDD',
    'Curso de Patrones de Diseño de Software'
)
ON CONFLICT DO NOTHING;

-- ============================================================
-- 11. PREGUNTAS DE DIAGNÓSTICO (25+ Preguntas Técnicas)
-- ============================================================
INSERT INTO preguntas_diagnostico (id_examen, id_categoria_pregunta, enunciado, nivel_dificultad, tipo_pregunta, puntaje, orden)
SELECT e.id_examen, cp.id_categoria_pregunta, v.enunciado, v.dificultad, 'OPCION_MULTIPLE', v.puntaje, v.orden_p
FROM examenes_diagnostico e
JOIN (VALUES
    -- Examen Android Native Developer
    ('Diagnóstico para Android Native Developer', 'Desarrollo Android y Móvil', '¿Cuál es el lenguaje recomendado oficialmente por Google para Android desde 2019?', 'BASICO', 1, 1),
    ('Diagnóstico para Android Native Developer', 'Desarrollo Android y Móvil', '¿Qué componente de Android se utiliza para representar una pantalla individual con interfaz?', 'BASICO', 1, 2),
    ('Diagnóstico para Android Native Developer', 'Desarrollo Android y Móvil', '¿Qué herramienta moderna de Android reemplaza a los layouts XML para construir UI declarativa?', 'INTERMEDIO', 2, 3),

    -- Examen Frontend Developer
    ('Diagnóstico para Frontend Developer', 'Desarrollo Web Frontend', '¿Qué etiqueta HTML semántica debe usarse para el menú de navegación principal?', 'BASICO', 1, 1),
    ('Diagnóstico para Frontend Developer', 'Desarrollo Web Frontend', '¿Qué tecnología de CSS permite layouts bidimensionales con filas y columnas explícitas?', 'BASICO', 1, 2),
    ('Diagnóstico para Frontend Developer', 'Desarrollo Web Frontend', '¿Qué hook de React se encarga de gestionar efectos secundarios como peticiones de red?', 'INTERMEDIO', 2, 3),

    -- Examen Backend con Node.js
    ('Diagnóstico para Backend Developer con Node.js', 'Desarrollo Web Backend', '¿Qué modelo de entrada/salida utiliza el runtime de Node.js?', 'INTERMEDIO', 2, 1),
    ('Diagnóstico para Backend Developer con Node.js', 'Desarrollo Web Backend', '¿Cuál es el framework HTTP minimalista más extendido en el ecosistema Node.js?', 'BASICO', 1, 2),
    ('Diagnóstico para Backend Developer con Node.js', 'Desarrollo Web Backend', '¿Qué formato es el estándar por defecto para el intercambio de datos en APIs REST?', 'BASICO', 1, 3),

    -- Examen DBA
    ('Diagnóstico para Database Administrator (DBA)', 'Bases de Datos Relacionales', '¿Qué tipo de índice es el predeterminado y más versátil en PostgreSQL?', 'INTERMEDIO', 2, 1),
    ('Diagnóstico para Database Administrator (DBA)', 'Bases de Datos Relacionales', '¿Qué comando SQL permite ver el costo y plan estimado de ejecución de una consulta?', 'AVANZADO', 3, 2),
    ('Diagnóstico para Database Administrator (DBA)', 'Bases de Datos Relacionales', '¿Qué propiedad de ACID asegura que una transacción completada sobreviva a fallos del sistema?', 'INTERMEDIO', 2, 3),

    -- Examen Cloud & DevOps
    ('Diagnóstico para Cloud & DevOps Engineer', 'Cloud & DevOps', '¿Qué archivo define la receta de construcción paso a paso de una imagen Docker?', 'BASICO', 1, 1),
    ('Diagnóstico para Cloud & DevOps Engineer', 'Cloud & DevOps', '¿Qué servicio de AWS ofrece almacenamiento de objetos escalable en la nube?', 'BASICO', 1, 2),

    -- Examen Cybersecurity Analyst
    ('Diagnóstico para Cybersecurity Analyst', 'Ciberseguridad y Hacking Ético', '¿Cuál es el puerto estándar y seguro utilizado por el protocolo HTTPS?', 'BASICO', 1, 1),
    ('Diagnóstico para Cybersecurity Analyst', 'Ciberseguridad y Hacking Ético', '¿Qué vulnerabilidad ocurre cuando un atacante inyecta scripts en páginas vistas por otros usuarios?', 'INTERMEDIO', 2, 2),

    -- Examen Testing
    ('Diagnóstico para QA Automation Engineer', 'Testing y Calidad de Código', '¿En qué consiste el primer paso (Red) del ciclo Red-Green-Refactor en TDD?', 'BASICO', 1, 1),
    ('Diagnóstico para QA Automation Engineer', 'Testing y Calidad de Código', '¿Qué tipo de prueba verifica que diferentes módulos o servicios operen correctamente juntos?', 'INTERMEDIO', 2, 2),

    -- Examen Arquitectura de Software
    ('Diagnóstico para Software Architect', 'Arquitectura de Software', '¿Qué principio SOLID establece que una clase debe tener una única razón para cambiar?', 'INTERMEDIO', 2, 1),
    ('Diagnóstico para Software Architect', 'Arquitectura de Software', '¿Qué patrón arquitectónico desacopla el almacenamiento de datos del modelo de dominio?', 'AVANZADO', 3, 2),

    -- Examen IA
    ('Diagnóstico para AI Solutions Developer', 'Fundamentos de Programación', '¿Qué técnica permite a un modelo LLM consultar documentos externos privados antes de responder?', 'AVANZADO', 3, 1),

    -- Examen Inglés Técnico
    ('Diagnóstico para English for Tech Specialist', 'Inglés Técnico para TI', '¿Qué significa el acrónimo API en inglés?', 'BASICO', 1, 1),

    -- Examen Móvil Multiplataforma
    ('Diagnóstico para Mobile Developer Multiplataforma', 'Desarrollo Android y Móvil', '¿En qué lenguaje de programación está escrito el framework Flutter?', 'BASICO', 1, 1),
    ('Diagnóstico para Mobile Developer Multiplataforma', 'Desarrollo Android y Móvil', '¿Qué comando de Flutter analiza el entorno del sistema y las dependencias instaladas?', 'BASICO', 1, 2),

    -- Examen Fullstack
    ('Diagnóstico para Fullstack Web Developer', 'Fundamentos de Programación', '¿Qué estructura de datos opera bajo el principio LIFO (Last In, First Out)?', 'BASICO', 1, 1)
) AS v(examen, categoria, enunciado, dificultad, puntaje, orden_p)
JOIN categorias_pregunta cp ON cp.nombre = v.categoria
ON e.nombre = v.examen
ON CONFLICT (id_examen, orden) DO UPDATE SET
    enunciado = EXCLUDED.enunciado,
    nivel_dificultad = EXCLUDED.nivel_dificultad,
    puntaje = EXCLUDED.puntaje;

-- ============================================================
-- 12. OPCIONES DE DIAGNÓSTICO (3 por pregunta = 75+ Opciones)
-- ============================================================
INSERT INTO opciones_diagnostico (id_pregunta, texto, es_correcta, orden)
SELECT p.id_pregunta, v.texto, v.es_correcta, v.orden_op
FROM preguntas_diagnostico p
JOIN (VALUES
    -- Android
    ('¿Cuál es el lenguaje recomendado oficialmente por Google para Android desde 2019?', 'Kotlin', TRUE, 1),
    ('¿Cuál es el lenguaje recomendado oficialmente por Google para Android desde 2019?', 'Java', FALSE, 2),
    ('¿Cuál es el lenguaje recomendado oficialmente por Google para Android desde 2019?', 'C++', FALSE, 3),

    ('¿Qué componente de Android se utiliza para representar una pantalla individual con interfaz?', 'Activity', TRUE, 1),
    ('¿Qué componente de Android se utiliza para representar una pantalla individual con interfaz?', 'BroadcastReceiver', FALSE, 2),
    ('¿Qué componente de Android se utiliza para representar una pantalla individual con interfaz?', 'ContentProvider', FALSE, 3),

    ('¿Qué herramienta moderna de Android reemplaza a los layouts XML para construir UI declarativa?', 'Jetpack Compose', TRUE, 1),
    ('¿Qué herramienta moderna de Android reemplaza a los layouts XML para construir UI declarativa?', 'Java Swing', FALSE, 2),
    ('¿Qué herramienta moderna de Android reemplaza a los layouts XML para construir UI declarativa?', 'Apache Cordova', FALSE, 3),

    -- Frontend
    ('¿Qué etiqueta HTML semántica debe usarse para el menú de navegación principal?', '<nav>', TRUE, 1),
    ('¿Qué etiqueta HTML semántica debe usarse para el menú de navegación principal?', '<menu-bar>', FALSE, 2),
    ('¿Qué etiqueta HTML semántica debe usarse para el menú de navegación principal?', '<section-links>', FALSE, 3),

    ('¿Qué tecnología de CSS permite layouts bidimensionales con filas y columnas explícitas?', 'CSS Grid', TRUE, 1),
    ('¿Qué tecnología de CSS permite layouts bidimensionales con filas y columnas explícitas?', 'Flexbox unidimensional', FALSE, 2),
    ('¿Qué tecnología de CSS permite layouts bidimensionales con filas y columnas explícitas?', 'Float Left', FALSE, 3),

    ('¿Qué hook de React se encarga de gestionar efectos secundarios como peticiones de red?', 'useEffect', TRUE, 1),
    ('¿Qué hook de React se encarga de gestionar efectos secundarios como peticiones de red?', 'useState', FALSE, 2),
    ('¿Qué hook de React se encarga de gestionar efectos secundarios como peticiones de red?', 'useMemo', FALSE, 3),

    -- Backend Node
    ('¿Qué modelo de entrada/salida utiliza el runtime de Node.js?', 'Asíncrono y no bloqueante basado en eventos', TRUE, 1),
    ('¿Qué modelo de entrada/salida utiliza el runtime de Node.js?', 'Multihilo síncrono por cada conexión', FALSE, 2),
    ('¿Qué modelo de entrada/salida utiliza el runtime de Node.js?', 'Secuencial bloqueante tradicional', FALSE, 3),

    ('¿Cuál es el framework HTTP minimalista más extendido en el ecosistema Node.js?', 'Express.js', TRUE, 1),
    ('¿Cuál es el framework HTTP minimalista más extendido en el ecosistema Node.js?', 'Django', FALSE, 2),
    ('¿Cuál es el framework HTTP minimalista más extendido en el ecosistema Node.js?', 'Laravel', FALSE, 3),

    ('¿Qué formato es el estándar por defecto para el intercambio de datos en APIs REST?', 'JSON', TRUE, 1),
    ('¿Qué formato es el estándar por defecto para el intercambio de datos en APIs REST?', 'XML binario', FALSE, 2),
    ('¿Qué formato es el estándar por defecto para el intercambio de datos en APIs REST?', 'CSV comprimido', FALSE, 3),

    -- DBA PostgreSQL
    ('¿Qué tipo de índice es el predeterminado y más versátil en PostgreSQL?', 'B-Tree', TRUE, 1),
    ('¿Qué tipo de índice es el predeterminado y más versátil en PostgreSQL?', 'Hash Index', FALSE, 2),
    ('¿Qué tipo de índice es el predeterminado y más versátil en PostgreSQL?', 'GIN espacial', FALSE, 3),

    ('¿Qué comando SQL permite ver el costo y plan estimado de ejecución de una consulta?', 'EXPLAIN (o EXPLAIN ANALYZE)', TRUE, 1),
    ('¿Qué comando SQL permite ver el costo y plan estimado de ejecución de una consulta?', 'SHOW PERFORMANCE', FALSE, 2),
    ('¿Qué comando SQL permite ver el costo y plan estimado de ejecución de una consulta?', 'DESCRIBE COST', FALSE, 3),

    ('¿Qué propiedad de ACID asegura que una transacción completada sobreviva a fallos del sistema?', 'Durabilidad (Durability)', TRUE, 1),
    ('¿Qué propiedad de ACID asegura que una transacción completada sobreviva a fallos del sistema?', 'Aislamiento (Isolation)', FALSE, 2),
    ('¿Qué propiedad de ACID asegura que una transacción completada sobreviva a fallos del sistema?', 'Atomicidad (Atomicity)', FALSE, 3),

    -- Cloud & DevOps
    ('¿Qué archivo define la receta de construcción paso a paso de una imagen Docker?', 'Dockerfile', TRUE, 1),
    ('¿Qué archivo define la receta de construcción paso a paso de una imagen Docker?', 'docker-compose.yml', FALSE, 2),
    ('¿Qué archivo define la receta de construcción paso a paso de una imagen Docker?', 'package.json', FALSE, 3),

    ('¿Qué servicio de AWS ofrece almacenamiento de objetos escalable en la nube?', 'Amazon S3 (Simple Storage Service)', TRUE, 1),
    ('¿Qué servicio de AWS ofrece almacenamiento de objetos escalable en la nube?', 'Amazon EC2', FALSE, 2),
    ('¿Qué servicio de AWS ofrece almacenamiento de objetos escalable en la nube?', 'AWS Lambda', FALSE, 3),

    -- Ciberseguridad
    ('¿Cuál es el puerto estándar y seguro utilizado por el protocolo HTTPS?', 'Puerto 443', TRUE, 1),
    ('¿Cuál es el puerto estándar y seguro utilizado por el protocolo HTTPS?', 'Puerto 80', FALSE, 2),
    ('¿Cuál es el puerto estándar y seguro utilizado por el protocolo HTTPS?', 'Puerto 21', FALSE, 3),

    ('¿Qué vulnerabilidad ocurre cuando un atacante inyecta scripts en páginas vistas por otros usuarios?', 'Cross-Site Scripting (XSS)', TRUE, 1),
    ('¿Qué vulnerabilidad ocurre cuando un atacante inyecta scripts en páginas vistas por otros usuarios?', 'SQL Injection', FALSE, 2),
    ('¿Qué vulnerabilidad ocurre cuando un atacante inyecta scripts en páginas vistas por otros usuarios?', 'DDoS Attack', FALSE, 3),

    -- Testing
    ('¿En qué consiste el primer paso (Red) del ciclo Red-Green-Refactor en TDD?', 'Escribir una prueba unitaria que falle antes de programar la funcionalidad', TRUE, 1),
    ('¿En qué consiste el primer paso (Red) del ciclo Red-Green-Refactor en TDD?', 'Desplegar la aplicación directamente a producción', FALSE, 2),
    ('¿En qué consiste el primer paso (Red) del ciclo Red-Green-Refactor en TDD?', 'Borrar el código anterior por completo', FALSE, 3),

    ('¿Qué tipo de prueba verifica que diferentes módulos o servicios operen correctamente juntos?', 'Prueba de Integración', TRUE, 1),
    ('¿Qué tipo de prueba verifica que diferentes módulos o servicios operen correctamente juntos?', 'Prueba Unitaria aislada', FALSE, 2),
    ('¿Qué tipo de prueba verifica que diferentes módulos o servicios operen correctamente juntos?', 'Prueba de Carga de red', FALSE, 3),

    -- Arquitectura
    ('¿Qué principio SOLID establece que una clase debe tener una única razón para cambiar?', 'Principio de Responsabilidad Única (SRP)', TRUE, 1),
    ('¿Qué principio SOLID establece que una clase debe tener una única razón para cambiar?', 'Principio de Inversión de Dependencias (DIP)', FALSE, 2),
    ('¿Qué principio SOLID establece que una clase debe tener una única razón para cambiar?', 'Principio Abierto/Cerrado (OCP)', FALSE, 3),

    ('¿Qué patrón arquitectónico desacopla el almacenamiento de datos del modelo de dominio?', 'Patrón Repository', TRUE, 1),
    ('¿Qué patrón arquitectónico desacopla el almacenamiento de datos del modelo de dominio?', 'Patrón Singleton', FALSE, 2),
    ('¿Qué patrón arquitectónico desacopla el almacenamiento de datos del modelo de dominio?', 'Patrón Prototype', FALSE, 3),

    -- IA
    ('¿Qué técnica permite a un modelo LLM consultar documentos externos privados antes de responder?', 'RAG (Retrieval-Augmented Generation)', TRUE, 1),
    ('¿Qué técnica permite a un modelo LLM consultar documentos externos privados antes de responder?', 'Zero-Shot Learning', FALSE, 2),
    ('¿Qué técnica permite a un modelo LLM consultar documentos externos privados antes de responder?', 'Fine-Tuning destructivo', FALSE, 3),

    -- Inglés
    ('¿Qué significa el acrónimo API en inglés?', 'Application Programming Interface', TRUE, 1),
    ('¿Qué significa el acrónimo API en inglés?', 'Automated Protocol Internetwork', FALSE, 2),
    ('¿Qué significa el acrónimo API en inglés?', 'Advanced Processing Instruction', FALSE, 3),

    -- Flutter
    ('¿En qué lenguaje de programación está escrito el framework Flutter?', 'Dart', TRUE, 1),
    ('¿En qué lenguaje de programación está escrito el framework Flutter?', 'TypeScript', FALSE, 2),
    ('¿En qué lenguaje de programación está escrito el framework Flutter?', 'Swift', FALSE, 3),

    ('¿Qué comando de Flutter analiza el entorno del sistema y las dependencias instaladas?', 'flutter doctor', TRUE, 1),
    ('¿Qué comando de Flutter analiza el entorno del sistema y las dependencias instaladas?', 'flutter inspect', FALSE, 2),
    ('¿Qué comando de Flutter analiza el entorno del sistema y las dependencias instaladas?', 'flutter check-health', FALSE, 3),

    -- Estructura de Datos
    ('¿Qué estructura de datos opera bajo el principio LIFO (Last In, First Out)?', 'Pila (Stack)', TRUE, 1),
    ('¿Qué estructura de datos opera bajo el principio LIFO (Last In, First Out)?', 'Cola (Queue)', FALSE, 2),
    ('¿Qué estructura de datos opera bajo el principio LIFO (Last In, First Out)?', 'Árbol Binario (Tree)', FALSE, 3)
) AS v(enunciado, texto, es_correcta, orden_op)
ON p.enunciado = v.enunciado
ON CONFLICT (id_pregunta, orden) DO UPDATE SET
    texto = EXCLUDED.texto,
    es_correcta = EXCLUDED.es_correcta;

-- ============================================================
-- 13. MISIONES DE GAMIFICACIÓN (25 Misiones Activas)
-- ============================================================
INSERT INTO misiones (nombre, descripcion, tipo, meta, xp_recompensa, activa) VALUES
('Primer Paso', 'Completa tu primera lección en cualquier curso', 'LECCIONES', 1, 50, TRUE),
('Doble Ración', 'Completa 2 lecciones en un solo día', 'DIARIA', 2, 80, TRUE),
('Tríada del Conocimiento', 'Finaliza 3 lecciones el día de hoy', 'DIARIA', 3, 120, TRUE),
('Maratón Diaria', 'Supera 5 lecciones en 24 horas', 'DIARIA', 5, 200, TRUE),
('Racha Inicial', 'Mantén tu racha de estudio activa por 2 días consecutivos', 'RACHA', 2, 70, TRUE),
('Hábito de Acero', 'Alcanza una racha continua de 5 días de estudio', 'RACHA', 5, 180, TRUE),
('Semana Invicta', 'Completa 7 días seguidos de aprendizaje', 'RACHA', 7, 300, TRUE),
('Constancia Quincenal', 'Mantén tu racha durante 14 días ininterrumpidos', 'RACHA', 14, 600, TRUE),
('Mes Imparable', 'Logra 30 días de racha de estudio activa', 'RACHA', 30, 1500, TRUE),
('Cosecha de 100 XP', 'Acumula tus primeros 100 puntos de experiencia', 'XP', 100, 50, TRUE),
('Impulso de 250 XP', 'Gana 250 puntos de experiencia en total', 'XP', 250, 100, TRUE),
('Veterano de 500 XP', 'Suma 500 puntos de XP en la plataforma', 'XP', 500, 220, TRUE),
('Maestría de 1000 XP', 'Supera los 1000 puntos de experiencia acumulados', 'XP', 1000, 500, TRUE),
('Leyenda de 2500 XP', 'Alcanza la cima con 2500 puntos de experiencia', 'XP', 2500, 1200, TRUE),
('Desafío Semanal de Lecciones', 'Completa 10 lecciones a lo largo de la semana', 'SEMANAL', 10, 350, TRUE),
('Cazador Semanal de XP', 'Acumula 600 XP durante esta semana', 'SEMANAL', 600, 400, TRUE),
('Perfección Semanal', 'Completa 15 lecciones en una semana', 'SEMANAL', 15, 600, TRUE),
('Primer Diagnóstico', 'Rinde tu primera prueba de nivelación o diagnóstico', 'ESPECIAL', 1, 150, TRUE),
('Explorador de Cursos', 'Inscríbete y avanza en al menos 2 cursos distintos', 'ESPECIAL', 2, 180, TRUE),
('Graduado Primer Curso', 'Completa al 100% todos los niveles de un curso', 'ESPECIAL', 1, 500, TRUE),
('Mente Lógica', 'Resuelve 5 ejercicios prácticos sin equivocarte', 'ESPECIAL', 5, 200, TRUE),
('Maestro del Código Móvil', 'Completa el nivel básico de desarrollo Android', 'ESPECIAL', 1, 250, TRUE),
('Arquitecto Frontend', 'Completa el curso definitivo de HTML y CSS', 'ESPECIAL', 1, 350, TRUE),
('Explorador de Base de Datos', 'Resuelve todos los ejercicios de PostgreSQL', 'ESPECIAL', 5, 220, TRUE),
('Búho Nocturno', 'Estudia y completa una lección después de las 10:00 PM', 'ESPECIAL', 1, 100, TRUE)
ON CONFLICT (nombre) DO UPDATE SET
    descripcion = EXCLUDED.descripcion,
    tipo = EXCLUDED.tipo,
    meta = EXCLUDED.meta,
    xp_recompensa = EXCLUDED.xp_recompensa,
    activa = TRUE;

-- ============================================================
-- 14. RECOMPENSAS / INSIGNIAS (25 Recompensas Desbloqueables)
-- ============================================================
INSERT INTO recompensas (nombre, descripcion, tipo, cantidad, costo_monedas, dia_secuencia_requerido, activa) VALUES
('Insignia Bienvenida', 'Bienvenido a TechMentor. Comienza tu viaje de aprendizaje', 'INSIGNIA', 1, 0, NULL, TRUE),
('Monedas de Bienvenida', 'Bolsa inicial de monedas para personalizar tu avatar', 'MONEDAS', 50, 0, NULL, TRUE),
('Insignia Primer Código', 'Otorgada al ejecutar y aprobar tu primer ejercicio', 'INSIGNIA', 1, 0, NULL, TRUE),
('Recompensa Racha Día 1', 'Premio diario por conectarte tu primer día', 'MONEDAS', 15, 0, 1, TRUE),
('Recompensa Racha Día 2', 'Premio diario por mantenerte activo 2 días seguidos', 'MONEDAS', 20, 0, 2, TRUE),
('Recompensa Racha Día 3', 'Premio diario por estudiar 3 días consecutivos', 'MONEDAS', 30, 0, 3, TRUE),
('Recompensa Racha Día 4', 'Premio diario por estudiar 4 días consecutivos', 'MONEDAS', 40, 0, 4, TRUE),
('Recompensa Racha Día 5', 'Premio diario por estudiar 5 días consecutivos', 'MONEDAS', 50, 0, 5, TRUE),
('Recompensa Racha Día 6', 'Premio diario por estudiar 6 días consecutivos', 'MONEDAS', 70, 0, 6, TRUE),
('Recompensa Racha Día 7 - Corona Semanal', 'Premio especial por completar una semana entera', 'MONEDAS', 150, 0, 7, TRUE),
('Insignia Racha de Fuego', 'Reconocimiento por alcanzar 10 días seguidos de racha', 'INSIGNIA', 1, 0, 10, TRUE),
('Insignia Quincenal', 'Reconocimiento por alcanzar 15 días continuos de racha', 'INSIGNIA', 1, 0, 15, TRUE),
('Insignia Titán Mensual', 'Reconocimiento élite por 30 días seguidos de estudio', 'INSIGNIA', 1, 0, 30, TRUE),
('Bono de Experiencia Bronce', 'Paquete de impulso de 100 puntos de XP', 'XP', 100, 40, NULL, TRUE),
('Bono de Experiencia Plata', 'Paquete de impulso de 250 puntos de XP', 'XP', 250, 90, NULL, TRUE),
('Bono de Experiencia Oro', 'Paquete de impulso de 600 puntos de XP', 'XP', 600, 200, NULL, TRUE),
('Escudo Protector de Racha', 'Protege tu racha de un día de inactividad accidental', 'ITEM', 1, 100, NULL, TRUE),
('Congelador de Racha Temporal', 'Mantiene congelada tu racha por 48 horas seguidas', 'ITEM', 1, 180, NULL, TRUE),
('Avatar Desarrollador Junior', 'Aspecto exclusivo de programador entusiasta', 'AVATAR', 1, 120, NULL, TRUE),
('Avatar Hacker Élite', 'Aspecto de especialista en ciberseguridad y terminal', 'AVATAR', 1, 250, NULL, TRUE),
('Avatar Astronauta Cloud', 'Aspecto espacial para arquitectos de la nube', 'AVATAR', 1, 300, NULL, TRUE),
('Insignia Maestro Android', 'Completaste la ruta técnica completa de desarrollo móvil', 'INSIGNIA', 1, 0, NULL, TRUE),
('Insignia Ninja Frontend', 'Completaste los cursos esenciales de maquetación y React', 'INSIGNIA', 1, 0, NULL, TRUE),
('Insignia Gurú SQL', 'Completaste todas las lecciones de bases de datos', 'INSIGNIA', 1, 0, NULL, TRUE),
('Trofeo Leyenda TechMentor', 'Máximo galardón otorgado a estudiantes destacados', 'TROFEO', 1, 500, NULL, TRUE)
ON CONFLICT (nombre) DO UPDATE SET
    descripcion = EXCLUDED.descripcion,
    tipo = EXCLUDED.tipo,
    cantidad = EXCLUDED.cantidad,
    costo_monedas = EXCLUDED.costo_monedas,
    dia_secuencia_requerido = EXCLUDED.dia_secuencia_requerido,
    activa = TRUE;

-- ============================================================
-- 15. REQUISITOS PROFESIÓN - CURSOS (25 Asignaciones)
-- ============================================================
INSERT INTO profesion_cursos (id_profesion, id_curso, prioridad, obligatorio)
SELECT p.id_profesion, c.id_curso, v.prioridad, v.obligatorio
FROM (VALUES
    -- Android Native Developer
    ('Android Native Developer', 'Curso de Kotlin Básico', 1, TRUE),
    ('Android Native Developer', 'Curso de Desarrollo de Apps con Android y Kotlin', 1, TRUE),
    ('Android Native Developer', 'Curso de Jetpack Compose Moderno', 1, TRUE),
    ('Android Native Developer', 'Curso Profesional de Git y GitHub', 2, FALSE),

    -- Frontend Developer
    ('Frontend Developer', 'Curso Definitivo de HTML y CSS', 1, TRUE),
    ('Frontend Developer', 'Curso Práctico de HTML y CSS', 1, TRUE),
    ('Frontend Developer', 'Curso Básico de JavaScript', 1, TRUE),
    ('Frontend Developer', 'Curso de React.js Básico', 1, TRUE),
    ('Frontend Developer', 'Curso de Figma para Desarrolladores', 2, FALSE),

    -- Backend Developer Node
    ('Backend Developer con Node.js', 'Curso Básico de JavaScript', 1, TRUE),
    ('Backend Developer con Node.js', 'Curso de Backend con Node.js y Express', 1, TRUE),
    ('Backend Developer con Node.js', 'Curso de Bases de Datos Relacionales con PostgreSQL', 1, TRUE),
    ('Backend Developer con Node.js', 'Curso de Docker y Contenedores', 2, FALSE),

    -- Mobile Multiplataforma
    ('Mobile Developer Multiplataforma', 'Curso Básico de Flutter', 1, TRUE),
    ('Mobile Developer Multiplataforma', 'Curso de Introducción a React Native', 1, TRUE),
    ('Mobile Developer Multiplataforma', 'Curso Básico de JavaScript', 2, FALSE),

    -- Database Administrator
    ('Database Administrator (DBA)', 'Curso de Bases de Datos Relacionales con PostgreSQL', 1, TRUE),
    ('Database Administrator (DBA)', 'Curso de Optimización y Consultas SQL Avanzadas', 1, TRUE),

    -- Cloud & DevOps
    ('Cloud & DevOps Engineer', 'Curso de Docker y Contenedores', 1, TRUE),
    ('Cloud & DevOps Engineer', 'Curso de Fundamentos de AWS Cloud', 1, TRUE),

    -- AI Solutions Developer
    ('AI Solutions Developer', 'Curso de Fundamentos de Inteligencia Artificial', 1, TRUE),
    ('AI Solutions Developer', 'Curso Práctico de Prompt Engineering y LLMs', 1, TRUE),

    -- QA Automation Engineer
    ('QA Automation Engineer', 'Curso de Pruebas Unitarias y TDD', 1, TRUE),
    ('QA Automation Engineer', 'Curso Básico de JavaScript', 2, FALSE),

    -- Software Architect
    ('Software Architect', 'Curso de Patrones de Diseño de Software', 1, TRUE)
) AS v(profesion, curso, prioridad, obligatorio)
JOIN profesiones p ON p.nombre = v.profesion
JOIN cursos c ON c.nombre = v.curso
ON CONFLICT (id_profesion, id_curso) DO UPDATE SET
    prioridad = EXCLUDED.prioridad,
    obligatorio = EXCLUDED.obligatorio;

-- ============================================================
-- 16. ASIGNACIONES DE MISIONES Y RECOMPENSAS AL ALUMNO PRINCIPAL
-- ============================================================
INSERT INTO misiones_usuario (id_usuario, id_mision, progreso_actual, completada, fecha_asignacion)
SELECT u.id_usuario, m.id_mision, v.progreso, v.completada, CURRENT_TIMESTAMP
FROM (VALUES
    ('Primer Paso', 1, TRUE),
    ('Doble Ración', 2, TRUE),
    ('Tríada del Conocimiento', 1, FALSE),
    ('Racha Inicial', 2, TRUE),
    ('Hábito de Acero', 5, TRUE),
    ('Semana Invicta', 7, TRUE),
    ('Cosecha de 100 XP', 100, TRUE),
    ('Impulso de 250 XP', 240, FALSE),
    ('Primer Diagnóstico', 1, TRUE),
    ('Explorador de Cursos', 2, TRUE),
    ('Desafío Semanal de Lecciones', 4, FALSE),
    ('Mente Lógica', 3, FALSE),
    ('Maestro del Código Móvil', 1, TRUE)
) AS v(mision, progreso, completada)
JOIN misiones m ON m.nombre = v.mision
CROSS JOIN (SELECT id_usuario FROM usuarios WHERE email = 'alumno@techmentor.test') u
ON CONFLICT (id_usuario, id_mision) DO UPDATE SET
    progreso_actual = EXCLUDED.progreso_actual,
    completada = EXCLUDED.completada;

INSERT INTO recompensas_usuario (id_usuario, id_recompensa, fecha_obtencion, reclamada)
SELECT u.id_usuario, r.id_recompensa, CURRENT_TIMESTAMP - INTERVAL '2 days', TRUE
FROM recompensas r
CROSS JOIN (SELECT id_usuario FROM usuarios WHERE email = 'alumno@techmentor.test') u
WHERE r.nombre IN (
    'Insignia Bienvenida',
    'Monedas de Bienvenida',
    'Insignia Primer Código',
    'Recompensa Racha Día 1',
    'Recompensa Racha Día 2',
    'Recompensa Racha Día 3',
    'Recompensa Racha Día 4',
    'Recompensa Racha Día 5',
    'Recompensa Racha Día 6',
    'Recompensa Racha Día 7 - Corona Semanal',
    'Insignia Maestro Android',
    'Insignia Ninja Frontend'
)
ON CONFLICT DO NOTHING;

-- ============================================================
-- 17. PROGRESO DE CURSOS Y LECCIONES PARA ALUMNO PRINCIPAL
-- ============================================================
INSERT INTO progreso_curso (id_usuario, id_curso, id_nivel_actual, fecha_inicio, fecha_ultima_actividad, estado, xp_ganado)
SELECT u.id_usuario, c.id_curso,
       (SELECT id_nivel FROM niveles WHERE id_curso = c.id_curso ORDER BY orden ASC LIMIT 1),
       CURRENT_TIMESTAMP - INTERVAL '5 days',
       CURRENT_TIMESTAMP,
       v.estado,
       v.xp
FROM (VALUES
    ('Curso de Kotlin Básico', 'EN_CURSO', 125),
    ('Curso de Desarrollo de Apps con Android y Kotlin', 'EN_CURSO', 90),
    ('Curso Definitivo de HTML y CSS', 'COMPLETADO', 350),
    ('Curso Práctico de HTML y CSS', 'COMPLETADO', 400),
    ('Curso Básico de JavaScript', 'EN_CURSO', 75),
    ('Curso de React.js Básico', 'NO_INICIADO', 0),
    ('Curso de Bases de Datos Relacionales con PostgreSQL', 'EN_CURSO', 60),
    ('Curso Gratis de Programación Básica', 'COMPLETADO', 300)
) AS v(curso, estado, xp)
JOIN cursos c ON c.nombre = v.curso
CROSS JOIN (SELECT id_usuario FROM usuarios WHERE email = 'alumno@techmentor.test') u
ON CONFLICT (id_usuario, id_curso) DO UPDATE SET
    estado = EXCLUDED.estado,
    xp_ganado = EXCLUDED.xp_ganado,
    fecha_ultima_actividad = CURRENT_TIMESTAMP;

INSERT INTO progreso_leccion (id_usuario, id_leccion, estado, puntaje, intentos, fecha_inicio, fecha_completado, xp_ganado)
SELECT u.id_usuario, l.id_leccion, 'COMPLETADO', 100, 1,
       CURRENT_TIMESTAMP - INTERVAL '2 days', CURRENT_TIMESTAMP - INTERVAL '1 day', l.xp_recompensa
FROM lecciones l
JOIN niveles n ON l.id_nivel = n.id_nivel
JOIN cursos c ON n.id_curso = c.id_curso
CROSS JOIN (SELECT id_usuario FROM usuarios WHERE email = 'alumno@techmentor.test') u
WHERE c.nombre IN ('Curso de Kotlin Básico', 'Curso Definitivo de HTML y CSS', 'Curso Básico de JavaScript')
  AND l.orden <= 3
ON CONFLICT (id_usuario, id_leccion) DO NOTHING;

-- Fin del script de población de 25 registros proporcionales
