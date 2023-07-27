FROM openjdk:17-jdk-slim
COPY target/budget-app.jar /budget-app.jar
ENTRYPOINT java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /budget-app.jar
