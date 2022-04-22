
# syntax=docker/dockerfile:1
# 1.基础镜像
FROM tticc/openjdk:8-alpine3.15.3

# 2.设置环境变量
# 大概率需要传入的：SERVER_PORT、NAMESRV_ADDR
ENV WORKDIR="/usr/local" \
    JVM_XMS="512m" \
    JVM_XMX="512m" \
    JVM_XMN="256m" \
    JVM_MS="128m" \
    JVM_MMS="320m" \
    SERVER_PORT="8080" \
    # 使用 @ 分割多个集群
    NAMESRV_ADDR="127.0.0.1:9876" \
    DATA_PATH="${WORKDIR}/data" \
    TIME_ZONE="Asia/Shanghai"

# 3.指定工作空间
WORKDIR ${WORKDIR}

# 4.复制启动jar包
COPY rocketmq-dashboard-18.jar app.jar


# 4.设置启动参数
ENTRYPOINT ["sh","-c","java -Xms${JVM_XMS} -Xmx${JVM_XMX} -Xmn${JVM_XMN} -XX:MetaspaceSize=${JVM_MS} -XX:MaxMetaspaceSize=${JVM_MMS} -jar app.jar --server.port=${SERVER_PORT} --rocketmq.config.namesrvAddrs=${NAMESRV_ADDR} --rocketmq.config.dataPath=${DATA_PATH}"]
