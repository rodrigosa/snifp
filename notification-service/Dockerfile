# Passo 1: Atualizado para buscar a imagem oficial do Java 21
FROM eclipse-temurin:21-jdk-alpine

# Passo 2: Define a pasta de trabalho interna
WORKDIR /app

# Passo 3: Copia o arquivo .jar compilado pelo Maven
COPY target/*.jar app.jar

# Passo 4: Comando para rodar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]