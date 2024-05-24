# syntax = docker/dockerfile:1.2

#
# Build stage
#
FROM maven:3.8.6-openjdk-18 AS build

# Copia el contenido de tu proyecto en el contenedor
COPY . .

# Ejecuta el empaquetado con Maven
RUN mvn clean package assembly:single -DskipTests

#
# Package stage
#
FROM openjdk:17-jdk-slim

# Copia el archivo JAR generado desde la etapa de compilación
COPY --from=build /target/my-app-name-1.0-SNAPSHOT-jar-with-dependencies.jar app.jar

# Establece el puerto en el que tu aplicación escuchará
EXPOSE 8080

# Define el punto de entrada de tu aplicación
ENTRYPOINT ["java", "-classpath", "app.jar", "ar.edu.utn.dds.k3003.app.WebApp"]