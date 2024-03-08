FROM openjdk:17-jdk-alpine
MAINTAINER joao.martins
COPY target/rinha-backend-0.0.1-SNAPSHOT.jar rinha-backend.jar
ENTRYPOINT ["java","-jar","/rinha-backend.jar"]