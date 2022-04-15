# syntax=docker/dockerfile:1
# FROM tticc/alpine:3.15.3
FROM tticc/alpine-glibc:latest

#1、指明该镜像的作者
MAINTAINER tticc

#2、在构建镜像时，指定镜像的工作目录，之后的命令都是基于此工作目录，如果不存在，则会创建目录
WORKDIR /usr/local


#3、添加redis安装包
ADD redis-6.0.16.tar.gz /usr/local

COPY commands.sh /usr/local/redis-6.0.16/commands.sh

#4、下载工具，按照redis，删除工具
RUN apk add make gcc g++ linux-headers && \
    cd redis-6.0.16 && \
    make && make install && \
    apk del make gcc g++ linux-headers && \
    mkdir config && \
    cp /usr/local/redis-6.0.16/redis.conf /usr/local/redis-6.0.16/config/redis.conf && \
    cp /usr/local/redis-6.0.16/sentinel.conf /usr/local/redis-6.0.16/config/sentinel.conf && \
    cp /usr/local/redis-6.0.16/commands.sh /usr/local/redis-6.0.16/config/commands.sh



#5、设置启动命令
#CMD ["redis-server", "/usr/local/redis-6.0.16/config/redis.conf"]
#CMD ["redis-server", "/usr/local/redis-6.0.16/config/sentinel.conf","--sentinel"]
#ENTRYPOINT ["redis-server", "/usr/local/redis-6.0.16/config/redis.conf","--port ${TPORT}"]
ENTRYPOINT ["sh","-c","/usr/local/redis-6.0.16/config/commands.sh"]
# Expose ports.
# EXPOSE 6379

# docker build -t t_redis .
# docker run -it t_redis:latest