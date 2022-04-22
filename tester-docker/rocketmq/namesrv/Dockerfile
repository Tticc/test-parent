# syntax=docker/dockerfile:1

FROM tticc/openjdk:8-alpine3.15.3

#1、指明该镜像的作者
MAINTAINER tticc

#3、设置环境变量
# 大概率需要传入的：无
ENV WORKDIR="/usr/local" \
    ROCKET_VERSION="4.5.0" \
    ROCKET_FORDER="rocketmq-all-4.5.0-bin-release"

#3、在构建镜像时，指定镜像的工作目录，之后的命令都是基于此工作目录，如果不存在，则会创建目录
WORKDIR ${WORKDIR}


#4、下载mq，解压、配置
RUN wget https://archive.apache.org/dist/rocketmq/${ROCKET_VERSION}/rocketmq-all-${ROCKET_VERSION}-bin-release.zip &&\
    unzip ${ROCKET_FORDER}.zip &&\
    rm ${ROCKET_FORDER}.zip &&\
    mkdir logs &&\
    mkdir store




#5、设置启动命令
ENTRYPOINT ["sh","-c","export JAVA_OPT=' -Duser.home=${WORKDIR}' && ${WORKDIR}/${ROCKET_FORDER}/bin/mqnamesrv"]


