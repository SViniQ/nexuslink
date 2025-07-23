# Estágio 1: Build da aplicação com Maven
# Usamos uma imagem oficial do Maven com JDK 17 para compilar o projeto
FROM maven:3.9.6-eclipse-temurin-17 AS build

# Define o diretório de trabalho dentro do contêiner
WORKDIR /app

# Copia o arquivo de configuração do Maven
COPY pom.xml .

# Copia o Maven Wrapper
COPY .mvn/ .mvn
COPY mvnw .
COPY mvnw.cmd .

# Copia o código-fonte do projeto
COPY src ./src

# Executa o build do Maven para gerar o arquivo .jar
# O -DskipTests pula a execução de testes para um build mais rápido
RUN mvn package -DskipTests

# Estágio 2: Execução da aplicação
# Usamos uma imagem base do Java 17, que é muito menor e mais segura
FROM eclipse-temurin:17-jre-jammy

# Define o diretório de trabalho
WORKDIR /app

# Copia o arquivo .jar gerado no estágio de build para o contêiner final
# O nome do JAR pode variar, então usamos um coringa
COPY --from=build /app/target/*.jar app.jar

# Expõe a porta 8080 para que a Render possa se comunicar com a aplicação
EXPOSE 8080

# Comando para iniciar a aplicação quando o contêiner for executado
ENTRYPOINT ["java", "-jar", "app.jar"]