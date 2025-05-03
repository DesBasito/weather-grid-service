FROM maven:3.9.8-amazoncorretto-21 AS build
WORKDIR /build
COPY src ./src
COPY pom.xml ./

RUN mvn clean package -e -DskipTests

FROM amazoncorretto:21
WORKDIR /app
COPY --from=build /build/target/weather-grid-service*jar ./weather-grid-service.jar
EXPOSE 9778
CMD ["java","-jar","weather-grid-service.jar"]