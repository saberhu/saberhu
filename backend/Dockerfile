







# ============================================================
# 阜阳台球约战小程序 - 后端 Dockerfile
# 基于已打包的 JAR 构建镜像，JDK 17 运行
# ============================================================

FROM openjdk:17-slim
WORKDIR /app

# 从本地 target 目录复制已打包的 JAR
COPY target/billiards-backend-1.0.0.jar app.jar

EXPOSE 8080

# 数据库配置通过环境变量注入（由 docker-compose 提供）
ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=prod"]








