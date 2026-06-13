



# ============================================================
# 阜阳台球约战小程序 - 后端 Dockerfile
# 基于 JDK 17 构建
# ============================================================

# 构建阶段
FROM maven:3.8-openjdk-17 AS builder
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn package -DskipTests -B

# 运行阶段
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
EXPOSE 8080

# 使用环境变量配置数据库连接
ENV MYSQL_HOST=db \
    MYSQL_PORT=3306 \
    MYSQL_DATABASE=billiards \
    MYSQL_USER=root \
    MYSQL_PASSWORD=root123

ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=dev", "app.jar"]



