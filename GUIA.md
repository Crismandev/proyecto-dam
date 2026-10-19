# TechMentor — guía obligatoria

Este archivo es la fuente de contexto del proyecto. Cualquier persona o IA que trabaje en este repositorio debe leerlo **antes** de cambiar código, inventar credenciales o tocar `ApiClient.kt`.

Si algo de esta guía choca con el `README.md` de la raíz, **gana esta guía**. El README tiene usuarios viejos que ya no existen.

---

## Qué es

Monorepo DAM: plataforma de aprendizaje (cursos, diagnóstico, gamificación).

| Parte | Stack | Puerto | Quién lo usa |
| --- | --- | --- | --- |
| API | Spring Boot 4.1.1, Java 21, Maven | `8080` | Web y móvil |
| Web admin | Angular 17 | `4200` | Solo `ADMINISTRADOR` |
| App | Android Kotlin + Retrofit | — | Alumno / VIP (staff ve panel de gestión) |
| BD | PostgreSQL `techmentor` | `5432` | Todo |

Rutas del repo:

```
backend/          API REST + JWT
frontend/         portal admin Angular
mobile-app/tech-mentor/   app Android
datos_prueba.sql  semilla de desarrollo (NO es Flyway)
techmentor.sql    esquema de 31 tablas
```

---

## Credenciales de desarrollo

El login es con **username**, no con el correo. `POST /auth/login` body: `{ "username", "password" }`.

| Rol | Username | Password | Email | Portal web | App móvil |
| --- | --- | --- | --- | --- | --- |
| ADMINISTRADOR | `admin` | `Admin123*` | admin@techmentor.test | Sí | Home de gestión (no progreso de alumno) |
| MAESTRO | `maestro` | `Maestro123*` | maestro@techmentor.test | No (el portal lo rechaza) | Home de gestión |
| ALUMNO | `alumno` | `Alumno123*` | alumno@techmentor.test | No | Home de estudiante (misiones propias) |
| USUARIO_VIP | `vip` | `Vip123*` | vip@techmentor.test | No | Home de estudiante |

Estas cuentas las crea `datos_prueba.sql` (hashes bcrypt con pgcrypto).

### Roles en seguridad

- `@EsStaff` → `ADMINISTRADOR` y `MAESTRO`
- `@EsAdmin` → solo `ADMINISTRADOR`
- `@EsEstudiante` → `ALUMNO` y `USUARIO_VIP` (unirse a misión, recompensas, exámenes)
- Autenticado (cualquier rol) → `GET /usuarios/me`, `GET /gamificacion/misiones`, `GET /gamificacion/mis-misiones`, `GET /catalogo/cursos`

**403 en Home móvil:** unirse a misión sigue siendo solo estudiante. Listar misiones ya no. Para ver progreso real entra con `alumno`, no con `admin`.

### PostgreSQL

| Dato | Valor por defecto en `application.yml` | Lo habitual en local |
| --- | --- | --- |
| Host | `localhost:5432` | igual |
| Base | `techmentor` | igual |
| Usuario | `postgres` (`DB_USERNAME`) | `postgres` |
| Password | `root` (`DB_PASSWORD`) | a menudo `postgres` |

Si tu Postgres no usa `root`, arranca el backend con la variable. No hace falta editar el yml.

```bash
# Linux / macOS
export SPRING_DATASOURCE_PASSWORD=postgres
# o
export DB_PASSWORD=postgres

# Windows PowerShell
$env:SPRING_DATASOURCE_PASSWORD="postgres"
```

---

## 1. Clonar / actualizar (no clones de nuevo si ya tienes el repo)

```bash
# Primera vez
git clone <url-del-repo>
cd proyecto-dam

# Ya tienes el repo: actualiza lo que subieron los compañeros
git pull --no-rebase origin main
```

Si tienes cambios locales, `stash` o commit antes del pull. No uses `git clone` otra vez encima de la carpeta de trabajo.

---

## 2. Base de datos (Win / Linux)

1. Instala PostgreSQL 14+.
2. Crea la base (una vez):

**Linux**

```bash
sudo -u postgres psql -c "CREATE DATABASE techmentor;"
# o, si tu usuario es postgres con password:
psql -h localhost -U postgres -c "CREATE DATABASE techmentor;"
```

**Windows** (pgAdmin o `psql` en PATH)

```bat
psql -h localhost -U postgres -c "CREATE DATABASE techmentor;"
```

3. Esquema (31 tablas), desde la raíz del repo:

```bash
psql -h localhost -U postgres -d techmentor -f techmentor.sql
```

4. Datos de prueba (después del esquema, se puede repetir con cuidado por `ON CONFLICT`):

```bash
psql -h localhost -U postgres -d techmentor -f datos_prueba.sql
```

`datos_prueba.sql` no es migración Flyway. No lo pongas en `db/migration`.

**Opcional — datos demo para el dashboard web** (24 estudiantes con registros en los últimos 12 meses, inscripciones y lecciones completadas). Correr después de `datos_prueba.sql`; es idempotente y solo crea correos `@demo.techmentor.test`:

```bash
psql -h localhost -U postgres -d techmentor -f datos_demo.sql
```

Usuario = parte del correo antes de `@` con `_` en vez de punto (ej. `lucia_ramirez`), contraseña `Demo123*`. Para quitarlos: `DELETE FROM usuarios WHERE email LIKE '%@demo.techmentor.test';`

---

## 3. Backend

Requisitos: **Java 21**, Maven 3.8+.

**Linux / macOS**

```bash
cd backend
# si tu password de postgres no es "root":
SPRING_DATASOURCE_PASSWORD=postgres mvn spring-boot:run
```

**Windows PowerShell**

```powershell
cd backend
$env:SPRING_DATASOURCE_PASSWORD="postgres"
mvn spring-boot:run
```

API: `http://localhost:8080`  
Login de prueba:

```bash
curl -s -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d "{\"username\":\"admin\",\"password\":\"Admin123*\"}"
```

Puerto: `PORT` o `8080`. CORS permite `http://localhost:4200` y `http://127.0.0.1:4200`.

---

## 4. Frontend (portal admin)

Requisitos: Node 18+, Angular CLI 17.

```bash
cd frontend
npm install
npx ng serve --host 127.0.0.1 --port 4200
```

Abre `http://127.0.0.1:4200` o `http://localhost:4200`. Entra con `admin` / `Admin123*`.

La URL del API está en `frontend/src/environments/environment.ts` (`http://localhost:8080`). En local no cambies eso por una IP: el navegador y el backend están en la misma máquina.

---

## 5. App Android — IP en `local.properties` (no en `ApiClient.kt`)

### Por qué ya no se cambia en `ApiClient.kt`

Antes cada integrante editaba a mano `BASE_URL` en:

`mobile-app/tech-mentor/app/src/main/java/pe/edu/cibertec/tech_mentor/data/remote/api/ApiClient.kt`

Eso chocaba en Git: uno subía `192.168.100.10`, otro `192.168.1.12`, el emulador necesita `10.0.2.2`. Cada pull rompía al resto.

Ahora:

1. `ApiClient.kt` lee `BuildConfig.API_BASE_URL` (no hay IP hardcodeada).
2. `app/build.gradle.kts` arma esa URL desde **`local.properties`**, que **está en `.gitignore`** y no se sube.
3. Cada máquina tiene su IP. El código compartido no cambia.

Plantilla: `mobile-app/tech-mentor/local.properties.example`.

### Cómo poner tu IP

Archivo (no se commitea):

`mobile-app/tech-mentor/local.properties`

```
sdk.dir=...ruta del Android SDK...
api.ip=192.168.1.12
```

Opcional:

```
api.port=8080
# o URL completa:
# api.base.url=http://192.168.1.12:8080/
```

| Entorno | `api.ip` | Notas |
| --- | --- | --- |
| Emulador Android | `10.0.2.2` o no pongas `api.ip` | Ese es el default del Gradle |
| Celular en la misma Wi‑Fi | IP LAN de **tu PC** | Linux: `hostname -I` · Windows: `ipconfig` → IPv4 |
| USB + `adb reverse tcp:8080 tcp:8080` | `127.0.0.1` | El teléfono habla con el puerto reenviado |

Después de cambiar `api.ip` hay que **Sync / Rebuild** el módulo `app`. Un hot-reload de Kotlin no regenera `BuildConfig`.

**No edites `ApiClient.kt` para cambiar la IP.** Tampoco lo envíes por chat: `BuildConfig` lo genera Gradle en `app/build.gradle.kts`. Sin ese archivo (y sin Sync) aparece `Unresolved reference: BuildConfig`.

Si el Gradle se queja de `Unresolved reference 'util'` al usar `java.util.Properties()`, el archivo ya debe tener `import java.util.Properties` al inicio (en Kotlin DSL `java.util` choca con el plugin Java).

Si el build falla en `extractDeepLinksDebug` / XML mal formado: `File → Sync Project with Gradle Files`, luego `Build → Clean Project` y `Rebuild`. No mezclar un `ApiClient.kt` suelto con un `nav_graph.xml` a medias.

---

## 6. Orden para levantar todo

1. PostgreSQL en marcha, base `techmentor` con esquema + `datos_prueba.sql`.
2. Backend en `:8080` (password de BD correcta).
3. Frontend en `:4200` si vas a usar el admin.
4. App: `local.properties` con tu IP → Rebuild → instalar. PC y teléfono en la misma red; firewall permitiendo 8080.

---

## Convenciones para IA y el equipo

- **No inventar usuarios.** Los únicos de prueba son los de la tabla de arriba.
- **No hardcodear IPs** en `ApiClient.kt` ni en commits.
- **No meter secretos** (`.env`, `local.properties`) a git.
- **No hacer push** ni commit salvo que Crisman / el autor lo pida.
- Actualizar el repo con `git pull`, no clonando otra carpeta.
- Web admin: emojis de acción no; iconos Remix (`ri-pencil-line`, `ri-delete-bin-line`, `ri-eye-line`).
- Home móvil: staff ve panel de catálogo; alumno ve progreso y misiones propias.
- Semilla extra (diagnóstico, misiones del alumno) vive en `datos_prueba.sql`. Si tocas seed, no borres bloques de compañeros: fusiona.

### Login API

```
POST /auth/login
{ "username": "alumno", "password": "Alumno123*" }
→ { token, username, rol, ... }
Header: Authorization: Bearer <token>
```

### JWT local

`application.yml` trae un `jwt.secret` de desarrollo. En producción va `JWT_SECRET`. No lo trates como password de usuario.
