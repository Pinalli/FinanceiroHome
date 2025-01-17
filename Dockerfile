FROM eclipse-temurin:17-jdk-alpine AS build
WORKDIR /workspace/app

# Copiar arquivos do projeto
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

# Tornar o mvnw executável e fazer o build
RUN chmod +x mvnw
RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
COPY --from=build /workspace/app/target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
