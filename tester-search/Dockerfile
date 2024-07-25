# syntax=docker/dockerfile:1

FROM tticc/openjdk:8-alpine3.15.3
#FROM openjdk:16-alpine3.13

MAINTAINER tticc

WORKDIR .

# 需要持久化数据时使用VOLUME命令
#VOLUME /tmp

COPY ./target/tester-search.jar app.jar

# set environment
ENV JVM_XMS="256m" \
    JVM_XMX="256m" \
    JVM_XMN="128m" \
    JVM_MS="64m" \
    JVM_MMS="128m" \
    activeEnv="docker" \
#    ES_HOST="es01" \
#    ES_PORT="9200" \
    ES_HOST="cv-search-1231722121.ap-southeast-2.bonsaisearch.net" \
    ES_PORT="443" \
    SPECIFIC_ANALYSER="standard" \
    ES_USERNAME="3906g77xl6" \
    ES_PASSWORD="ew6glg92d5" \
    ES_CLIENTTYPE="bonsai" \
    TIME_ZONE="Asia/Shanghai"


#ENTRYPOINT exec java -jar app.jar --dev
ENTRYPOINT ["sh","-c","java -Xms${JVM_XMS} -Xmx${JVM_XMX} -Xmn${JVM_XMN} -XX:MetaspaceSize=${JVM_MS} -XX:MaxMetaspaceSize=${JVM_MMS} -jar app.jar --my.es.host=${ES_HOST} --my.es.port=${ES_PORT} --my.es.specificAnalyser=${SPECIFIC_ANALYSER} --my.es.username=${ES_USERNAME} --my.es.password=${ES_PASSWORD} --my.es.client.type=${ES_CLIENTTYPE}" ]
