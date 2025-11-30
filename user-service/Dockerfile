# 로컬에서 미리 빌드한 JAR 파일을 사용
# 빌드 명령어: ./gradlew :user-service:bootJar
FROM eclipse-temurin:21-jre

WORKDIR /app

# 빌드된 JAR 파일 복사
COPY build/libs/*.jar app.jar

# 포트 노출
EXPOSE 8081

# 애플리케이션 실행
ENTRYPOINT ["java", "-Duser.timezone=Asia/Seoul", "-jar", "app.jar"]