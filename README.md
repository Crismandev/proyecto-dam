# TechMentor 

Proyecto de aprendizaje tecnológico con portal administrativo y API REST.

## Requisitos
- Java 17+
- Maven 3.8+
- PostgreSQL 14+
- Node.js 18+ y Angular CLI 17+

## Configuración de Base de Datos
La base de datos utiliza PostgreSQL. Existe un script en `database/setup_db.sh` para levantar la estructura y datos semilla.

1. Asegúrate de tener el usuario `postgres` con contraseña `postgres` en PostgreSQL en `localhost:5432`.
2. Ejecuta el script `database/setup_db.sh` para crear la DB `techmentor` e importar el esquema y datos iniciales.

## Ejecutar Backend
```bash
cd backend
mvn spring-boot:run
```
El backend estará disponible en `http://localhost:8080`.

## Usuarios de Prueba
### Administrador (Portal Web)
- Correo: `admin@techmentor.pe`
- Clave: `Admin123*`

### Estudiantes (App Móvil)
- Correo: `luis@correo.com`
- Clave: `Luis12345*`

## Ejecutar Frontend (Portal Administrativo)
```bash
cd frontend
npm install
ng serve
```
El frontend estará disponible en `http://localhost:4200`.
