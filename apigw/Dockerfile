FROM openjdk:19
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} apigw.jar
ENTRYPOINT ["java","-jar","/apigw.jar"]
EXPOSE 8083