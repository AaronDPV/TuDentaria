# ==========================================
# STAGE 1: Compilar la aplicación Spring Boot
# ==========================================
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# Copiar pom.xml y dependencias
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copiar código fuente y compilar JAR
COPY src ./src
RUN mvn clean package -DskipTests

# ==========================================
# STAGE 2: Imagen final para ejecución (Debian glibc para DNS y red estables)
# ==========================================
FROM eclipse-temurin:17-jre
WORKDIR /app

# Crear directorio para subida de fotos
RUN mkdir -p uploads && chmod 777 uploads

# Copiar el JAR generado
COPY --from=build /app/target/tudentaria-*.jar app.jar

# Exponer el puerto predeterminado
EXPOSE 8080

# Variables de entorno
ENV PORT=8080 \
    SPRING_JPA_SHOW_SQL=false

# Comando de inicio con soporte IPv4 garantizado para redes cloud
ENTRYPOINT ["java", "-Djava.net.preferIPv4Stack=true", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]
