# TechMentor — Requerimientos del Proyecto

## Descripción General
Plataforma de aprendizaje tecnológico gamificado compuesta por:
- **Backend REST API**: Spring Boot 3.2.x (Java 17) con autenticación JWT
- **Frontend (Portal Administrativo)**: Angular 17
- **Base de Datos**: PostgreSQL 14+

---

## Sistema Operativo / Herramientas Globales

| Herramienta   | Versión Mínima | Uso                              |
|---------------|---------------|----------------------------------|
| Java (JDK)    | 17+           | Compilar y ejecutar el backend   |
| Maven         | 3.8+          | Gestor de dependencias Java      |
| Node.js       | 18+           | Runtime para Angular             |
| npm           | 9+            | Gestor de paquetes frontend      |
| Angular CLI   | 17+           | CLI para Angular                 |
| PostgreSQL    | 14+           | Base de datos relacional         |

---

## Base de Datos (PostgreSQL)

- Host: localhost | Puerto: 5432 | DB: techmentor
- Usuario: postgres | Contraseña: postgres

### Scripts SQL
- database/01_schema.sql  — esquema (tablas, índices)
- database/02_data.sql    — datos semilla
- database/setup_db.sh    — setup automático

### Tablas
roles, usuarios, categorias, cursos, niveles, lecciones,
ejercicios, opciones_ejercicio, progreso_leccion

---

## Backend (Spring Boot 3.2.5 / Java 17)

Directorio: backend/  |  Puerto: 8080

### Dependencias Maven (pom.xml)

| Dependencia                     | Versión | Propósito                 |
|---------------------------------|---------|---------------------------|
| spring-boot-starter-web         | 3.2.5   | API REST con Spring MVC   |
| spring-boot-starter-data-jpa    | 3.2.5   | JPA/Hibernate ORM         |
| spring-boot-starter-validation  | 3.2.5   | Validaciones Bean         |
| spring-boot-starter-security    | 3.2.5   | Seguridad y Auth          |
| postgresql (driver)             | runtime | Conector JDBC PostgreSQL  |
| lombok                          | opt.    | Reducción de boilerplate  |
| spring-boot-devtools            | runtime | Hot reload desarrollo     |
| jjwt-api                        | 0.12.5  | JSON Web Tokens API       |
| jjwt-impl                       | 0.12.5  | JWT implementación        |
| jjwt-jackson                    | 0.12.5  | JWT serialización Jackson |

### Variables de Entorno
DB_URL=jdbc:postgresql://localhost:5432/techmentor
DB_USER=postgres
DB_PASS=postgres
JWT_SECRET=<clave_hex_256bit>

### Comando
cd backend && mvn spring-boot:run

---

## Frontend Angular 17 (Portal Administrativo)

Directorio: frontend/  |  Puerto: 4200

### Dependencias npm (package.json)

#### Producción
| Paquete                            | Versión   | Propósito                 |
|------------------------------------|-----------|---------------------------|
| @angular/core                      | ^17.3.0   | Núcleo Angular            |
| @angular/common                    | ^17.3.0   | Módulos comunes           |
| @angular/compiler                  | ^17.3.0   | Compilador AOT/JIT        |
| @angular/forms                     | ^17.3.0   | Formularios               |
| @angular/router                    | ^17.3.0   | Enrutamiento SPA          |
| @angular/animations                | ^17.3.0   | Animaciones               |
| @angular/platform-browser          | ^17.3.0   | Plataforma browser        |
| @angular/platform-browser-dynamic  | ^17.3.0   | Compilación dinámica      |
| rxjs                               | ~7.8.0    | Programación reactiva     |
| tslib                              | ^2.3.0    | Runtime TypeScript        |
| zone.js                            | ~0.14.3   | Detección de cambios      |

#### Desarrollo
| Paquete                        | Versión   | Propósito              |
|--------------------------------|-----------|------------------------|
| @angular-devkit/build-angular  | ^17.3.17  | Builder/compilación    |
| @angular/cli                   | ^17.3.17  | Angular CLI            |
| @angular/compiler-cli          | ^17.3.0   | Compilador CLI         |
| typescript                     | ~5.4.2    | Lenguaje TypeScript    |
| karma                          | ~6.4.0    | Test runner            |
| karma-chrome-launcher          | ~3.2.0    | Chrome para tests      |
| karma-coverage                 | ~2.2.0    | Cobertura de tests     |
| karma-jasmine                  | ~5.1.0    | Jasmine en Karma       |
| karma-jasmine-html-reporter    | ~2.1.0    | Reporte HTML tests     |
| jasmine-core                   | ~5.1.0    | Framework de testing   |
| @types/jasmine                 | ~5.1.0    | Tipos Jasmine          |

### Comandos
cd frontend && npm install
ng serve

---

## Usuarios de Prueba

| Rol           | Correo              | Contraseña | Portal     |
|---------------|---------------------|------------|------------|
| Administrador | admin@techmentor.pe | Admin123*  | Web/Angular|
| Estudiante    | luis@correo.com     | Luis12345* | App Móvil  |

---

## Pasos para Levantar el Proyecto

1. Tener PostgreSQL corriendo con usuario postgres/postgres
2. Ejecutar: bash database/setup_db.sh
3. Backend:  cd backend && mvn spring-boot:run
4. Frontend: cd frontend && npm install && ng serve
