FROM openjdk:21-jdk

# 빌드된 JAR 복사
COPY build/libs/backend-0.0.1-SNAPSHOT.jar app.jar

# Spring Boot 앱 실행
ENTRYPOINT ["java", "-Dspring.profiles.active=docker", "-jar", "app.jar"]
