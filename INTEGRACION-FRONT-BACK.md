# Integración TechMentor

- El frontend Angular usa `http://localhost:8080` como base de API (`frontend/src/environments/environment.ts`).
- El backend Spring Boot debe estar disponible en el puerto 8080 y con su conexión PostgreSQL configurada en `backend/src/main/resources/application.properties`.
- Para desarrollo local, el backend acepta solicitudes desde `http://localhost:4200`.
- El login usa nombre de usuario y contraseña, como define `LoginDto`.
- La navegación del panel incluye las áreas con endpoints REST disponibles. Se retiró Dashboard porque el backend no incluye un endpoint de resumen global; Progreso se consulta por estudiante, según los endpoints disponibles.
