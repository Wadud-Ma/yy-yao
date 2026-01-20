# 构建阶段
FROM ghcr.io/wadud-ma/maven:3.6.3-jdk-21 as build
WORKDIR /app

# 首先复制 Maven 配置文件以利用 Docker 缓存
COPY settings.xml pom.xml /app/
COPY yy-yao-model/pom.xml /app/yy-yao-model/
COPY yy-yao-core/pom.xml /app/yy-yao-core/
COPY yy-yao-dao/pom.xml /app/yy-yao-dao/
COPY yy-yao-service/pom.xml /app/yy-yao-service/
COPY yy-yao-web/pom.xml /app/yy-yao-web/
COPY yy-yao-start/pom.xml /app/yy-yao-start/

# 下载依赖（利用 Docker 缓存）
RUN mvn -s /app/settings.xml dependency:go-offline -B

# 复制源代码
COPY yy-yao-model/src /app/yy-yao-model/src
COPY yy-yao-core/src /app/yy-yao-core/src
COPY yy-yao-dao/src /app/yy-yao-dao/src
COPY yy-yao-service/src /app/yy-yao-service/src
COPY yy-yao-web/src /app/yy-yao-web/src
COPY yy-yao-start/src /app/yy-yao-start/src

# 构建项目（完全跳过测试编译和执行以加快构建速度）
RUN mvn -s /app/settings.xml clean package -Dmaven.test.skip=true -B

# 运行阶段
FROM ghcr.io/wadud-ma/java:21-jre
WORKDIR /app

# 复制构建产物
COPY --from=build /app/yy-yao-start/target/yy-yao-start-*.jar /app/app.jar

# 暴露端口
# 此处端口必须与「服务设置」-「流水线」以及「手动上传代码包」部署时填写的端口一致，否则会部署失败。
EXPOSE 8080

# JVM 参数优化
ENV JAVA_OPTS="-XX:+UseContainerSupport \
               -XX:MaxRAMPercentage=75.0 \
               -XX:InitialRAMPercentage=50.0 \
               -XX:+UseG1GC \
               -XX:+UseStringDeduplication \
               -Djava.security.egd=file:/dev/./urandom"

# 启动应用
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]