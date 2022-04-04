# syntax=docker/dockerfile:1

FROM tticc/openjdk:8-alpine3.15.3
#FROM openjdk:16-alpine3.13

MAINTAINER tticc

WORKDIR .

# 需要持久化数据时使用VOLUME命令
#VOLUME /tmp

COPY ./target/tester-webapp.jar app.jar

#ENTRYPOINT exec java -jar app.jar --dev
ENTRYPOINT ["sh","-c","java -jar app.jar --spring.profiles.active=$activeEnv"]
