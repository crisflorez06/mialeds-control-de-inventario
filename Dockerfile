FROM eclipse-temurin:21.0.5_11-jdk
EXPOSE 8080
WORKDIR /app

# Copiar pom.xml primero
COPY pom.xml ./

# Copiar archivos del Maven Wrapper
COPY mvnw ./
COPY .mvn ./.mvn/

# Corregir finales de línea y dar permisos de ejecución
RUN sed -i 's/\r$//' mvnw && chmod +x mvnw

# Verificar que el archivo existe y tiene permisos
RUN ls -la mvnw

# Descargar dependencias
RUN ./mvnw -B dependency:go-offline

# Copiar el código fuente
COPY src ./src

# Compilar el proyecto
RUN ./mvnw clean install -DskipTests

# Ejecutar el .jar generado
ENTRYPOINT ["java", "-jar", "target/mialeds-0.0.1-SNAPSHOT.jar"]