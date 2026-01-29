FROM gradle:9.0-jdk17-alpine AS BUILD
WORKDIR /app
COPY . .
RUN gradle build --no-daemon

FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

COPY --from=build /app/build/libs/*.jar ./usuario.jar
RUN apk add --no-cache tzdata
ENV TZ=America/Sao_Paulo

EXPOSE 8081

CMD ["java", "-jar", "/app/usuario.jar"]