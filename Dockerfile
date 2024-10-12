FROM maven:19 as build
WORKDIR /app
COPY pom.xml ./
COPY src ./
RUN mvn clean install

FROM ams
WORKDIR /app

