FROM openjdk:19
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} fund-transfer-service.jar
ENTRYPOINT ["java","-jar","/fund-transfer-service.jar"]
EXPOSE 8112