# syntax=docker/dockerfile:1
# FROM tticc/alpine:3.15.3
FROM tticc/alpine-glibc:3.15.3

#1、指明该镜像的作者
MAINTAINER tticc

#2、在构建镜像时，指定镜像的工作目录，之后的命令都是基于此工作目录，如果不存在，则会创建目录
WORKDIR /usr/local

ENV CONFIG_PATH="/usr/local/redis-6.0.16/inner_config" \
    REDIS_PORT=6379 \
    BIND=0.0.0.0 \
    DATABASES=1 \
    DAEMONIZE=no \
    CLUSTER_ENABLED=no \
    ANNOUNCE_IP=0.0.0.0 \
    ANNOUNCE_PORT=6379 \
    ANNOUNCE_BUS_PORT=16379 \
    TIME_ZONE="Asia/Shanghai"

# 4、指定data为匿名卷，避免启动时不挂载导致数据丢失
# 为避免产生大量的匿名卷，暂时注释掉。在compose已经配置了宿主机挂载
# VOLUME ${WORKDIR}/data


#3、添加redis安装包
ADD redis-6.0.16.tar.gz /usr/local
# ADD 命令会把压缩包解压，所以无需tar命令进行解压
# tar zxvf redis-6.0.9.tar.gz

COPY commands.sh /usr/local/redis-6.0.16/commands.sh
COPY redis.conf /usr/local/redis-6.0.16/redis.conf

#4、下载工具，按照redis，删除工具
RUN sed -i 's/dl-cdn.alpinelinux.org/mirrors.ustc.edu.cn/g' /etc/apk/repositories && \
    apk add make gcc g++ linux-headers && \
    mkdir data && \
    cd redis-6.0.16 && \
    make && make install && \
    apk del make gcc g++ linux-headers && \
    mkdir inner_config && \
    cp /usr/local/redis-6.0.16/redis.conf /usr/local/redis-6.0.16/inner_config/redis.conf && \
    cp /usr/local/redis-6.0.16/sentinel.conf /usr/local/redis-6.0.16/inner_config/sentinel.conf && \
    cp /usr/local/redis-6.0.16/commands.sh /usr/local/redis-6.0.16/inner_config/commands.sh



#5、设置启动命令
#CMD ["redis-server", "/usr/local/redis-6.0.16/config/redis.conf"]
#CMD ["redis-server", "/usr/local/redis-6.0.16/config/sentinel.conf","--sentinel"]
#ENTRYPOINT ["redis-server", "/usr/local/redis-6.0.16/config/redis.conf","--port ${TPORT}"]
#ENTRYPOINT ["sh","-c","/usr/local/redis-6.0.16/out_config/commands.sh"]
ENTRYPOINT ["sh","-c","${CONFIG_PATH}/commands.sh"]
# Expose ports.
# EXPOSE 6379

# docker build -t t_redis .
# docker run -it t_redis:latest