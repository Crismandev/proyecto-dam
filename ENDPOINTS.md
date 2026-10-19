# Endpoints de TechMentor

Todas las rutas (menos `POST /auth/login`) llevan el header `Authorization: Bearer <token>`.

| Quién puede | Significado |
|---|---|
| PUBLIC | Público |
| AUTH | Cualquier usuario con token |
| STAFF | ADMINISTRADOR, MAESTRO |
| ADMIN | ADMINISTRADOR |
| ESTUDIANTE | ALUMNO, USUARIO_VIP |
| OWNER | Dueño del recurso o ADMINISTRADOR/MAESTRO |

Total: **166 endpoints** en 28 grupos.

## Seguridad - Roles

| Método | Ruta | Quién | Qué hace |
|---|---|---|---|
| GET | `/seguridad/roles` | STAFF | Listar |
| GET | `/seguridad/roles/1` | STAFF | Obtener por id |
| POST | `/seguridad/roles` | ADMIN | Crear |
| PUT | `/seguridad/roles/1` | ADMIN | Actualizar |
| DELETE | `/seguridad/roles/1` | ADMIN | Eliminar |

## Seguridad - Estados de usuario

| Método | Ruta | Quién | Qué hace |
|---|---|---|---|
| GET | `/seguridad/estados-usuario` | STAFF | Listar |
| GET | `/seguridad/estados-usuario/1` | STAFF | Obtener por id |
| POST | `/seguridad/estados-usuario` | ADMIN | Crear |
| PUT | `/seguridad/estados-usuario/1` | ADMIN | Actualizar |
| DELETE | `/seguridad/estados-usuario/1` | ADMIN | Eliminar |

## Catálogo - Categorías

| Método | Ruta | Quién | Qué hace |
|---|---|---|---|
| GET | `/catalogo/categorias` | AUTH | Listar activos |
| GET | `/catalogo/categorias/todos` | STAFF | Listar todos (incluye inactivos) |
| GET | `/catalogo/categorias/1` | AUTH | Obtener por id |
| POST | `/catalogo/categorias` | STAFF | Crear |
| PUT | `/catalogo/categorias/1` | STAFF | Actualizar |
| PATCH | `/catalogo/categorias/1/activo?valor=false` | STAFF | Activar / desactivar |
| DELETE | `/catalogo/categorias/1` | ADMIN | Eliminar |

## Catálogo - Cursos

| Método | Ruta | Quién | Qué hace |
|---|---|---|---|
| GET | `/catalogo/cursos?idCategoria=1` | AUTH | Listar activos (filtro opcional) |
| GET | `/catalogo/cursos/todos` | STAFF | Listar todos (incluye inactivos) |
| GET | `/catalogo/cursos/1` | AUTH | Obtener por id |
| POST | `/catalogo/cursos` | STAFF | Crear |
| PUT | `/catalogo/cursos/1` | STAFF | Actualizar |
| PATCH | `/catalogo/cursos/1/activo?valor=false` | STAFF | Activar / desactivar |
| DELETE | `/catalogo/cursos/1` | ADMIN | Eliminar |

## Catálogo - Niveles

| Método | Ruta | Quién | Qué hace |
|---|---|---|---|
| GET | `/catalogo/niveles` | AUTH | Listar |
| GET | `/catalogo/cursos/1/niveles` | AUTH | Listar por curso |
| GET | `/catalogo/niveles/1` | AUTH | Obtener por id |
| POST | `/catalogo/niveles` | STAFF | Crear |
| PUT | `/catalogo/niveles/1` | STAFF | Actualizar |
| DELETE | `/catalogo/niveles/1` | STAFF | Eliminar |

## Catálogo - Lecciones

| Método | Ruta | Quién | Qué hace |
|---|---|---|---|
| GET | `/catalogo/lecciones` | AUTH | Listar |
| GET | `/catalogo/niveles/1/lecciones` | AUTH | Listar por nivel |
| GET | `/catalogo/lecciones/1` | AUTH | Obtener por id |
| POST | `/catalogo/lecciones` | STAFF | Crear |
| PUT | `/catalogo/lecciones/1` | STAFF | Actualizar |
| DELETE | `/catalogo/lecciones/1` | STAFF | Eliminar |

## Catálogo - Recursos de lección (videos/textos)

| Método | Ruta | Quién | Qué hace |
|---|---|---|---|
| GET | `/catalogo/recursos` | STAFF | Listar |
| GET | `/catalogo/lecciones/1/recursos` | STAFF | Listar por leccion |
| GET | `/catalogo/recursos/1` | STAFF | Obtener por id |
| POST | `/catalogo/recursos` | STAFF | Crear |
| PUT | `/catalogo/recursos/1` | STAFF | Actualizar |
| PATCH | `/catalogo/recursos/1/activo?valor=false` | STAFF | Activar / desactivar |
| DELETE | `/catalogo/recursos/1` | STAFF | Eliminar |

## Catálogo - Ejercicios

| Método | Ruta | Quién | Qué hace |
|---|---|---|---|
| GET | `/catalogo/ejercicios` | STAFF | Listar |
| GET | `/catalogo/lecciones/1/ejercicios` | STAFF | Listar por leccion |
| GET | `/catalogo/ejercicios/1` | STAFF | Obtener por id |
| POST | `/catalogo/ejercicios` | STAFF | Crear |
| PUT | `/catalogo/ejercicios/1` | STAFF | Actualizar |
| DELETE | `/catalogo/ejercicios/1` | STAFF | Eliminar |

## Catálogo - Opciones de ejercicio

| Método | Ruta | Quién | Qué hace |
|---|---|---|---|
| GET | `/catalogo/opciones-ejercicio` | STAFF | Listar |
| GET | `/catalogo/ejercicios/1/opciones` | STAFF | Listar por ejercicio |
| GET | `/catalogo/opciones-ejercicio/1` | STAFF | Obtener por id |
| POST | `/catalogo/opciones-ejercicio` | STAFF | Crear |
| PUT | `/catalogo/opciones-ejercicio/1` | STAFF | Actualizar |
| DELETE | `/catalogo/opciones-ejercicio/1` | STAFF | Eliminar |

## Catálogo - Profesiones (puestos)

| Método | Ruta | Quién | Qué hace |
|---|---|---|---|
| GET | `/catalogo/profesiones` | AUTH | Listar activos |
| GET | `/catalogo/profesiones/todos` | STAFF | Listar todos (incluye inactivos) |
| GET | `/catalogo/profesiones/1` | AUTH | Obtener por id |
| POST | `/catalogo/profesiones` | STAFF | Crear |
| PUT | `/catalogo/profesiones/1` | STAFF | Actualizar |
| PATCH | `/catalogo/profesiones/1/activo?valor=false` | STAFF | Activar / desactivar |
| DELETE | `/catalogo/profesiones/1` | ADMIN | Eliminar |

## Catálogo - Cursos exigidos por puesto

| Método | Ruta | Quién | Qué hace |
|---|---|---|---|
| GET | `/catalogo/profesion-cursos` | AUTH | Listar |
| GET | `/catalogo/profesiones/1/cursos` | AUTH | Listar por profesion |
| GET | `/catalogo/profesion-cursos/1` | AUTH | Obtener por id |
| POST | `/catalogo/profesion-cursos` | STAFF | Crear |
| PUT | `/catalogo/profesion-cursos/1` | STAFF | Actualizar |
| DELETE | `/catalogo/profesion-cursos/1` | STAFF | Eliminar |

## Diagnóstico - Curso que refuerza cada categoría

| Método | Ruta | Quién | Qué hace |
|---|---|---|---|
| GET | `/diagnostico/categoria-pregunta-cursos` | STAFF | Listar |
| GET | `/diagnostico/categorias-pregunta/1/cursos` | STAFF | Listar por categoriaPregunta |
| GET | `/diagnostico/categoria-pregunta-cursos/1` | STAFF | Obtener por id |
| POST | `/diagnostico/categoria-pregunta-cursos` | STAFF | Crear |
| PUT | `/diagnostico/categoria-pregunta-cursos/1` | STAFF | Actualizar |
| DELETE | `/diagnostico/categoria-pregunta-cursos/1` | STAFF | Eliminar |

## Gamificación - Misiones

| Método | Ruta | Quién | Qué hace |
|---|---|---|---|
| GET | `/gamificacion/misiones` | AUTH | Listar activos |
| GET | `/gamificacion/misiones/todos` | STAFF | Listar todos (incluye inactivos) |
| GET | `/gamificacion/misiones/1` | AUTH | Obtener por id |
| POST | `/gamificacion/misiones` | STAFF | Crear |
| PUT | `/gamificacion/misiones/1` | STAFF | Actualizar |
| PATCH | `/gamificacion/misiones/1/activo?valor=false` | STAFF | Activar / desactivar |
| DELETE | `/gamificacion/misiones/1` | ADMIN | Eliminar |

## Gamificación - Recompensas

| Método | Ruta | Quién | Qué hace |
|---|---|---|---|
| GET | `/gamificacion/recompensas` | AUTH | Listar activos |
| GET | `/gamificacion/recompensas/todos` | STAFF | Listar todos (incluye inactivos) |
| GET | `/gamificacion/recompensas/1` | AUTH | Obtener por id |
| POST | `/gamificacion/recompensas` | STAFF | Crear |
| PUT | `/gamificacion/recompensas/1` | STAFF | Actualizar |
| PATCH | `/gamificacion/recompensas/1/activo?valor=false` | STAFF | Activar / desactivar |
| DELETE | `/gamificacion/recompensas/1` | ADMIN | Eliminar |

## Autenticación

| Método | Ruta | Quién | Qué hace |
|---|---|---|---|
| POST | `/auth/login` | PUBLIC | Login |
| POST | `/auth/registro` | PUBLIC | Auto-registro de alumno |
| PUT | `/auth/cambiar-clave` | AUTH | Cambiar mi contraseña |

## Usuarios

| Método | Ruta | Quién | Qué hace |
|---|---|---|---|
| GET | `/usuarios` | STAFF | Listar usuarios |
| GET | `/usuarios/me` | AUTH | Mi perfil |
| PUT | `/usuarios/me` | AUTH | Editar mi perfil |
| GET | `/usuarios/1` | OWNER | Obtener usuario por id |
| POST | `/usuarios` | ADMIN | Crear usuario (con credencial) |
| PUT | `/usuarios/1` | ADMIN | Editar perfil de un usuario |
| PATCH | `/usuarios/3/estado` | ADMIN | Cambiar estado (desactivar/bloquear/reactivar) |
| PATCH | `/usuarios/3/rol` | ADMIN | Cambiar rol |
| DELETE | `/usuarios/3` | ADMIN | Eliminar usuario |
| GET | `/usuarios/3/historial-estados` | STAFF | Historial de estados |

## Usuarios - Mis profesiones (puesto al que postulo)

| Método | Ruta | Quién | Qué hace |
|---|---|---|---|
| GET | `/usuarios/me/profesiones` | AUTH | Mis profesiones |
| POST | `/usuarios/me/profesiones` | AUTH | Asignarme una profesión |
| PATCH | `/usuarios/me/profesiones/1/principal` | AUTH | Marcar profesión principal |
| DELETE | `/usuarios/me/profesiones/1` | AUTH | Quitar una profesión |
| GET | `/usuarios/3/profesiones` | STAFF | Profesiones de un usuario |

## Diagnóstico - Categorías de pregunta

| Método | Ruta | Quién | Qué hace |
|---|---|---|---|
| GET | `/diagnostico/categorias-pregunta` | AUTH | Listar |
| GET | `/diagnostico/categorias-pregunta/1` | AUTH | Obtener por id |
| POST | `/diagnostico/categorias-pregunta` | STAFF | Crear |
| PUT | `/diagnostico/categorias-pregunta/1` | STAFF | Actualizar |
| DELETE | `/diagnostico/categorias-pregunta/1` | STAFF | Eliminar |

## Diagnóstico - Exámenes (adaptados al puesto)

| Método | Ruta | Quién | Qué hace |
|---|---|---|---|
| GET | `/diagnostico/examenes?idProfesion=1` | AUTH | Listar activos (?idCurso= ?idProfesion=) |
| GET | `/diagnostico/examenes/para-mi` | ESTUDIANTE | Exámenes para mi puesto |
| GET | `/diagnostico/examenes/todos` | STAFF | Listar todos (incluye inactivos) |
| GET | `/diagnostico/examenes/1` | AUTH | Obtener por id |
| GET | `/diagnostico/examenes/1/rendir` | AUTH | Ver examen para rendir (sin respuestas correctas) |
| POST | `/diagnostico/examenes` | STAFF | Crear |
| PUT | `/diagnostico/examenes/1` | STAFF | Actualizar / activar / desactivar |
| DELETE | `/diagnostico/examenes/1` | STAFF | Eliminar |

## Diagnóstico - Preguntas

| Método | Ruta | Quién | Qué hace |
|---|---|---|---|
| GET | `/diagnostico/examenes/1/preguntas` | STAFF | Listar preguntas de un examen |
| GET | `/diagnostico/preguntas/1` | STAFF | Obtener por id |
| POST | `/diagnostico/preguntas` | STAFF | Crear |
| PUT | `/diagnostico/preguntas/1` | STAFF | Actualizar |
| DELETE | `/diagnostico/preguntas/1` | STAFF | Eliminar |

## Diagnóstico - Opciones

| Método | Ruta | Quién | Qué hace |
|---|---|---|---|
| GET | `/diagnostico/preguntas/1/opciones` | STAFF | Listar opciones de una pregunta (con es_correcta) |
| GET | `/diagnostico/opciones/1` | STAFF | Obtener por id |
| POST | `/diagnostico/opciones` | STAFF | Crear |
| PUT | `/diagnostico/opciones/1` | STAFF | Actualizar |
| DELETE | `/diagnostico/opciones/1` | STAFF | Eliminar |

## Diagnóstico - Rendir y resultados

| Método | Ruta | Quién | Qué hace |
|---|---|---|---|
| POST | `/diagnostico/examenes/1/rendir` | ESTUDIANTE | Rendir examen (calcula puntaje y asigna nivel) |
| GET | `/diagnostico/resultados/mis-resultados` | ESTUDIANTE | Mis resultados |
| GET | `/diagnostico/resultados/1` | OWNER | Resultado por id |
| GET | `/diagnostico/resultados/usuario/3` | STAFF | Resultados de un usuario |
| GET | `/diagnostico/resultados/examen/1` | STAFF | Resultados de un examen |
| PUT | `/diagnostico/resultados/1/nivel` | STAFF | Cambiar nivel asignado manualmente |
| DELETE | `/diagnostico/resultados/1` | ADMIN | Eliminar resultado |
| GET | `/diagnostico/resultados/1/respuestas` | OWNER | Respuestas de un resultado |
| GET | `/diagnostico/respuestas/1` | OWNER | Respuesta por id |

## Recomendaciones de cursos

| Método | Ruta | Quién | Qué hace |
|---|---|---|---|
| POST | `/recomendaciones/generar/1` | OWNER | Generar recomendaciones de un resultado |
| GET | `/recomendaciones/mis-recomendaciones` | ESTUDIANTE | Mis recomendaciones |
| GET | `/recomendaciones/resultado/1` | OWNER | Recomendaciones de un resultado |
| GET | `/recomendaciones/resultado/1/analisis` | OWNER | Análisis por categoría de un resultado |
| GET | `/recomendaciones/usuario/3` | STAFF | Recomendaciones de un usuario |
| PATCH | `/recomendaciones/1/estado?valor=INICIADA` | OWNER | Cambiar estado (PENDIENTE/INICIADA/DESCARTADA) |

## Curso completo (al abrirlo)

| Método | Ruta | Quién | Qué hace |
|---|---|---|---|
| GET | `/catalogo/cursos/1/contenido` | AUTH | Contenido del curso: niveles, lecciones, videos, textos y ejercicios |

## Progreso - Cursos

| Método | Ruta | Quién | Qué hace |
|---|---|---|---|
| POST | `/progreso/cursos/1/iniciar` | ESTUDIANTE | Iniciar curso |
| PUT | `/progreso/cursos/1/abandonar` | ESTUDIANTE | Abandonar curso |
| GET | `/progreso/cursos/mis-cursos` | ESTUDIANTE | Mis cursos |
| GET | `/progreso/cursos/mis-cursos/1` | ESTUDIANTE | Mi progreso en un curso |
| GET | `/progreso/cursos/1` | OWNER | Progreso por id |
| GET | `/progreso/cursos/usuario/3` | STAFF | Progreso de un usuario |
| GET | `/progreso/cursos/curso/1` | STAFF | Progreso de todos en un curso |
| DELETE | `/progreso/cursos/1` | ADMIN | Eliminar progreso |

## Progreso - Lecciones

| Método | Ruta | Quién | Qué hace |
|---|---|---|---|
| POST | `/progreso/lecciones/1/iniciar` | ESTUDIANTE | Iniciar lección |
| POST | `/progreso/lecciones/1/completar` | ESTUDIANTE | Completar lección |
| GET | `/progreso/lecciones/mis-lecciones?idCurso=1` | ESTUDIANTE | Mis lecciones (?idCurso=) |
| GET | `/progreso/lecciones/1` | OWNER | Progreso de lección por id |
| GET | `/progreso/lecciones/usuario/3` | STAFF | Progreso de lecciones de un usuario |
| DELETE | `/progreso/lecciones/1` | ADMIN | Eliminar progreso de lección |

## Gamificación - Mis misiones

| Método | Ruta | Quién | Qué hace |
|---|---|---|---|
| POST | `/gamificacion/misiones/1/unirse` | ESTUDIANTE | Unirme a una misión |
| GET | `/gamificacion/mis-misiones` | ESTUDIANTE | Mis misiones |
| GET | `/gamificacion/usuarios/3/misiones` | STAFF | Misiones de un usuario |
| PUT | `/gamificacion/misiones-usuario/1/progreso` | STAFF | Fijar progreso de una misión |

## Gamificación - Mis recompensas

| Método | Ruta | Quién | Qué hace |
|---|---|---|---|
| POST | `/gamificacion/recompensas/1/obtener` | ESTUDIANTE | Obtener una recompensa |
| GET | `/gamificacion/mis-recompensas` | ESTUDIANTE | Mis recompensas |
| PATCH | `/gamificacion/mis-recompensas/1/reclamar` | ESTUDIANTE | Reclamar recompensa |
| GET | `/gamificacion/usuarios/3/recompensas` | STAFF | Recompensas de un usuario |
