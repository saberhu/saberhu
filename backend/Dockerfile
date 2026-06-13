








# ============================================================
# 阜阳台球约战小程序 - 后端 Dockerfile
# 使用 Maven 构建，JDK 17 运行
# ============================================================

# 构建阶段
FROM maven:3.8.7-openjdk-17-slim AS builder
WORKDIR /build
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn package -DskipTests -Pprod

# 运行阶段
FROM openjdk:17-slim
WORKDIR /app
COPY --from=builder /build/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=prod"]








