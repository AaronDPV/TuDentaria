# ==========================================
# STAGE 1: Compilar la aplicación Spring Boot
# ==========================================
FROM maven:3.9.6-eclipse-temurin-17-alpine AS build
WORKDIR /app

# Copiar pom.xml y descargar dependencias (para aprovechar caché de Docker)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copiar código fuente y compilar JAR
COPY src ./src
RUN mvn clean package -DskipTests

# ==========================================
# STAGE 2: Imagen final ligera para ejecución
# ==========================================
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Crear directorio para subida de fotos
RUN mkdir -p uploads && chmod 777 uploads

# Copiar el JAR generado desde la etapa de compilación
COPY --from=build /app/target/tudentaria-*.jar app.jar

# Exponer el puerto predeterminado
EXPOSE 8080

# Variables de entorno por defecto
ENV PORT=8080 \
    SPRING_JPA_SHOW_SQL=false

# Comando de inicio
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]
