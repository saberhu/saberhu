







# ============================================================
# 阜阳台球约战小程序 - 后端 Dockerfile
# 多阶段构建：Maven 编译 → JRE 运行
# ============================================================

# 构建阶段
FROM maven:3.8.7-eclipse-temurin-17-alpine AS builder
WORKDIR /build
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn package -DskipTests -Pprod

# 运行阶段
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=builder /build/target/*.jar app.jar
EXPOSE 8080

# 数据库配置通过环境变量注入（由 docker-compose 提供）
ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=prod"]








