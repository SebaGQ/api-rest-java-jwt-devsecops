# Etapa de construcción
FROM openjdk:17-slim as builder

# Establecer el directorio de trabajo
WORKDIR /workspace/app

# Copiar el archivo pom.xml y descargar las dependencias
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
RUN ./mvnw dependency:go-offline

# Copiar el código fuente y compilar el jar
COPY src src
RUN ./mvnw package -DskipTests

# Etapa de ejecución, crear la imagen final
FROM openjdk:17-slim

VOLUME /tmp

# Copiar el jar creado con mvnw
COPY --from=builder /workspace/app/target/medical-hour-management-0.0.1-SNAPSHOT.jar app.jar

# Exponer puerto para permitir comunicación desde fuera
EXPOSE 8080

# Definir el punto de entrada.
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
