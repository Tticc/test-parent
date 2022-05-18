# syntax=docker/dockerfile:1

FROM tticc/openjdk:8-alpine3.15.3
#FROM openjdk:16-alpine3.13

MAINTAINER tticc

WORKDIR .

# 需要持久化数据时使用VOLUME命令
#VOLUME /tmp

COPY apollo-adminservice-2.0.0-SNAPSHOT.jar app.jar

# set environment
ENV JVM_XMS="512m" \
    JVM_XMX="512m" \
    JVM_XMN="256m" \
    JVM_MS="128m" \
    JVM_MMS="320m" \
    EUREKA_INSTANCE_HOSTNAME="localhost" \
    TIME_ZONE="Asia/Shanghai"


#ENTRYPOINT exec java -jar app.jar --dev
ENTRYPOINT ["sh","-c","java -Xms${JVM_XMS} -Xmx${JVM_XMX} -Xmn${JVM_XMN} -XX:MetaspaceSize=${JVM_MS} -XX:MaxMetaspaceSize=${JVM_MMS} -jar app.jar --eureka.instance.hostname=${EUREKA_INSTANCE_HOSTNAME}" ]
