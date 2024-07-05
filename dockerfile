FROM openjdk:17-jdk-slim
EXPOSE ${SPRINGBOOT_PORT}
ADD ./build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "-Duser.timezone=Asia/Seoul", "/app.jar"]