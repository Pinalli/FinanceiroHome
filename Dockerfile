FROM eclipse-temurin:17-jdk-alpine AS build
WORKDIR /workspace/app

# Copiar arquivos do projeto
#COPY src src
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn

# Baixar dependências Maven
RUN ./mvnw dependency:go-offline -B

# Copiar o restante do projeto (depois de baixar as dependências)
COPY src src

# Tornar o mvnw executável e fazer o build
RUN chmod +x mvnw
RUN ./mvnw clean package -DskipTests

# Estágio de runtime
FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
COPY --from=build /workspace/app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]
