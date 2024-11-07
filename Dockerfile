# 베이스 이미지 설정 (Java 17 사용)
FROM openjdk:17-jdk-slim

# 작업 디렉터리 생성
WORKDIR /app

# JAR 파일을 컨테이너에 복사
COPY target/BACK-END-PROMPREN-0.0.1-SNAPSHOT.jar app.jar

# 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "/app/app.jar"]

# 포트 노출 (필요 시)
EXPOSE 8080