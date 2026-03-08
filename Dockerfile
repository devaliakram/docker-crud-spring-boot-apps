FROM eclipse-temurin:8-jdk
WORKDIR /apps
COPY ./target/spring-boot-crud-example-0.0.1-SNAPSHOT.jar /apps/spring-boot-crud-example.jar
EXPOSE 9192
ENTRYPOINT ["java","-jar","/apps/spring-boot-crud-example.jar"]