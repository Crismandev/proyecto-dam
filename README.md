# Debatir nombre del aplicativo

Plataforma de orientacion tecnica, preparacion laboral y aprendizaje integral. El ecosistema consta de una aplicacion movil Android nativa, un portal web administrativo en Angular y un backend en Spring Boot conectado a una base de datos relacional en la nube.

---

## Integrantes

- Coordinador: [Nombre y Apellidos]
- Integrante 2: [Nombre y Apellidos]
- Integrante 3: [Nombre y Apellidos]
- Integrante 4: [Nombre y Apellidos]
- Integrante 5: [Nombre y Apellidos]
- Integrante 6: [Nombre y Apellidos]
- Integrante 7: Cristhian Mantilla Albuquerque 

---

## Arquitectura (posibles librerias a usar guiado por IA)

```
[ Android App (Kotlin) ]
         |
         | (HTTP / Retrofit)
         v
[ API REST (Spring Boot) ] <---> [ BD en la nube (MySQL / PostgreSQL) ]
         ^
         | (HTTP / REST)
         |
[ Portal Web (Angular) ]
```

---

## Propuesta de Stack Tecnologico

- **Movil**: Android Studio, Kotlin, Coroutines, Retrofit 2, Room / SQLite, Material Components.
- **Web Admin**: Angular 17+, TypeScript, RxJS, Angular Material / Bootstrap.
- **Backend**: Java 17+, Spring Boot (Spring Web, Spring Data JPA, Spring Security, Swagger).
- **Base de Datos y Despliegue**: MySQL / PostgreSQL en hosting remoto (Aiven / Supabase / Neon / Render).

---

## Guia de Cumplimiento de Rubrica

Para asegurar la maxima nota segun la rubrica oficial del curso, el equipo debe implementar estrictamente los siguientes puntos en cada modulo:

### 1. SQLite Local en Android (4.0 Puntos)
La rubrica penaliza con menos nota si no estan las 4 operaciones.
- **Entidad asignada**: `HistorialEvaluacion` o `CursosFavoritos`.
- **Operaciones obligatorias**:
  - `Insert`: Guardar el resultado del quiz ('Evaluar mi nivel') o cursos guardados en local.
  - `Select`: Listar intentos previos y filtrar por fecha o nivel obtenido.
  - `Update`: Permitir editar notas personales o reintentar y actualizar puntaje.
  - `Delete`: Eliminar historial local antes o despues de sincronizar.
- **Sincronizacion**: Boton 'Sincronizar' para despachar registros locales pendientes hacia la API REST.

### 2. Consumo de Servicios Web REST (4.0 Puntos)
La rubrica exige mas de un servicio REST consumido en la app movil.
- **Endpoints minimos a consumir desde Retrofit**:
  - `GET /api/cursos`: Obtiene el catalogo con filtros por tecnologia (React, Spring Boot, MySQL, etc.).
  - `GET /api/evaluaciones/preguntas`: Descarga el banco de preguntas dinamico para el quiz.
  - `POST /api/evaluaciones/sincronizar`: Envia resultados calculados del telefono al servidor.
  - `POST /api/auth/login`: Autenticacion de cuenta de usuario.

### 3. Manejo de Listas Personalizadas (2.0 Puntos)
- **Implementacion**: `RecyclerView` en la pantalla de Cursos con `CustomAdapter` y `ViewHolder`.
- **Elementos visuales requeridos**: Imagen/icono del curso, titulo, nivel (Principiante/Intermedio), valoracion con estrellas y badge de tecnologia.

### 4. Layouts y Vistas (2.0 Puntos)
Implementar el 100% de los layouts mostrados en el diseno:
- `activity_login.xml`: Login con campos de correo, contrasena y botones sociales.
- `activity_home.xml`: Grid de accesos (Cursos, Asesoria 1:1, Simulacion, Evaluacion).
- `activity_evaluacion.xml`: Selector de preguntas, opciones y barra de avance.
- `activity_resultado.xml`: Badge de nivel (Intermedio), tarjeta de cursos sugeridos.
- `activity_cursos.xml`: Buscador superior, filtros horizontales y lista vertical optimizada.

### 5. Portal Web Administrativo y Base de Datos (Requisito Obligatorio)
- Mantenimiento CRUD completo en Angular para cada tabla: `Cursos`, `Preguntas/Respuestas`, `Categorias` y `Usuarios`.
- Base de datos relacional alojada en un hosting gratuito accesible por el backend.

### 6. Documentacion Formal del Informe (8.0 Puntos)
- **Analisis SEPTE**: Diagnostico usando al menos 3 variables (Social, Economica, Tecnologica) con fuentes y citas al pie de pagina sobre la brecha laboral y preparacion en TI.
- **Objetivos SMART**: Plantear minimo 2 objetivos especificos, medibles, alcanzables, realistas y temporales.

---

## Modulos Funcionales

### App Movil
- Autenticacion y sesion de usuario.
- Dashboard interactivo de navegacion.
- Evaluacion de nivel tecnico con feedback inmediato.
- Catalogo de cursos con busqueda reactiva y filtros.
- Persistencia local y envio de datos diferido (offline-first).

### Portal Web
- Dashboard con metricas de uso y rendimiento en evaluaciones.
- Gestion de cursos, niveles y material de estudio.
- Banco de preguntas para evaluacion de nivel.
- Control de roles y perfiles administrativos.

---

## Estructura del Repositorio

```
tech-mentor/
├── backend-api/             # API REST Spring Boot
│   ├── src/main/java/
│   └── pom.xml
├── frontend-admin/          # Portal Web Angular
│   ├── src/app/
│   └── package.json
├── mobile-app/              # App Android Studio (Kotlin)
│   ├── app/src/main/
│   └── build.gradle.kts
└── docs/                    # Informe, evidencias SEPTE y diagramas
```

---

## Puesta en Marcha

### Backend (Spring Boot)
```bash
cd backend-api
./mvnw clean spring-boot:run
```
Servicio disponible por defecto en: `http://localhost:8080`

### Frontend Web (Angular)
```bash
cd frontend-admin
npm install
ng serve
```
Portal disponible en: `http://localhost:4200`

### App Movil (Android)
1. Abrir `mobile-app` en Android Studio.
2. Sincronizar Gradle.
3. Definir la IP local o dominio en la configuracion de Retrofit (`BASE_URL`).
4. Ejecutar en emulador o dispositivo fisico 
