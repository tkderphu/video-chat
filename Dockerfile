
FROM maven:18 as build
WORKDIR /app
COPY pom.xml /.
COPY src /.
COPY ./mvnw /.
RUN ./mvnw build

