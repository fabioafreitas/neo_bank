# Use a imagem base do JDK 21
FROM eclipse-temurin:21-jdk AS build

# Define o diretório de trabalho
WORKDIR /app

# Copia apenas os arquivos necessários para baixar as dependências do Gradle
COPY gradlew /app/gradlew
COPY gradle /app/gradle
COPY build.gradle /app/build.gradle

# Adiciona um volume para o cache do Gradle
VOLUME /root/.gradle

# Baixa as dependências do Gradle (isso será armazenado no cache)
RUN sed -i 's/\r$//' gradlew
RUN ./gradlew dependencies --no-daemon

# Copia o restante do código do projeto
COPY . /app

# Executa o build do projeto, ignorando os testes
RUN ./gradlew build -x test --no-daemon

# Define a imagem final para execução
FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY --from=build /app/build/libs/backend-spring-0.0.1-SNAPSHOT.jar app.jar

# Define o perfil ativo do Spring
ENV SPRING_PROFILES_ACTIVE=test

# Comando para executar o aplicativo
CMD ["java", "-jar", "app.jar"]