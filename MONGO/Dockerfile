# Utilizăm imaginea de bază OpenJDK pentru Java 17
FROM openjdk:17-jdk-slim

# Setăm directorul de lucru
WORKDIR /app

# Instalăm mysql-client pentru debugging
RUN apt-get update && apt-get install -y --no-install-recommends \
    default-mysql-client \
    && rm -rf /var/lib/apt/lists/*

# Copiem fișierul JAR al serviciului Mongo
COPY target/MONGO-POS-API.jar app.jar

# Expunem portul pe care rulează aplicația
EXPOSE 8081

# Comanda pentru a rula aplicația
ENTRYPOINT ["java", "-jar", "app.jar"]
