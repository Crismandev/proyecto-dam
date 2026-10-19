# ========================================================
# Dockerfile para TechMentor Spring Boot Backend (Java 21)
# ========================================================
# Etapa 1: Compilación con Maven Wrapper
FROM eclipse-temurin:21-jdk AS builder
WORKDIR /app
COPY . .
RUN chmod +x ./mvnw
RUN ./mvnw clean package -DskipTests

# Etapa 2: Imagen final liviana de ejecución
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar

# El puerto se lee de la variable PORT (por defecto 8080)
EXPOSE 8080
ENV PORT=8080

ENTRYPOINT ["sh", "-c", "java -Dserver.port=${PORT} -jar app.jar"]
