# syntax=docker/dockerfile:1

FROM tticc/openjdk:8-alpine3.15.3

#1、指明该镜像的作者
MAINTAINER tticc

#3、设置环境变量
# 大概率需要传入的：NAME_SRV、BROKER_CONF
ENV WORKDIR="/usr/local" \
    ROCKET_VERSION="4.5.0" \
    ROCKET_FORDER="rocketmq-all-4.5.0-bin-release" \
    # 多个用;分割
    NAME_SRV="127.0.0.1:9876" \
    BROKER_CONF="${ROCKET_FORDER}/conf/broker.conf"
#    BROKER_CONF="/2m-2s-async/broker-a.properties"
#    BROKER_CONF="/2m-2s-async/broker-a-s.properties"
#    BROKER_CONF="/2m-2s-async/broker-b.properties"
#    BROKER_CONF="/2m-2s-async/broker-b-s.properties"

#3、在构建镜像时，指定镜像的工作目录，之后的命令都是基于此工作目录，如果不存在，则会创建目录
WORKDIR ${WORKDIR}

# 4、指定store为匿名卷，避免启动时不挂载导致数据丢失
# 为避免产生大量的匿名卷，暂时注释掉。在compose已经配置了宿主机挂载
# VOLUME ${WORKDIR}/store

#4、下载mq，解压、配置
RUN wget https://archive.apache.org/dist/rocketmq/${ROCKET_VERSION}/rocketmq-all-${ROCKET_VERSION}-bin-release.zip &&\
    unzip ${ROCKET_FORDER}.zip &&\
    rm ${ROCKET_FORDER}.zip &&\
    mkdir logs &&\
    mkdir store



#5、设置启动命令
ENTRYPOINT ["sh","-c","export JAVA_OPT=' -Duser.home='${WORKDIR} && ${WORKDIR}/${ROCKET_FORDER}/bin/mqbroker -n ${NAME_SRV} -c ${WORKDIR}/${BROKER_CONF}"]


