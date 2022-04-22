
# syntax=docker/dockerfile:1
#FROM adoptopenjdk/openjdk8
FROM tticc/openjdk:8-alpine3.15.3
WORKDIR .
COPY ./target/tester-async.jar app.jar


# set environment
ENV JVM_XMS="512m" \
    JVM_XMX="512m" \
    JVM_XMN="256m" \
    JVM_MS="128m" \
    JVM_MMS="320m" \
    activeEnv="docker" \
    TIME_ZONE="Asia/Shanghai"

#ENTRYPOINT exec java -jar app.jar --dev
ENTRYPOINT ["sh","-c","java -Xms${JVM_XMS} -Xmx${JVM_XMX} -Xmn${JVM_XMN} -XX:MetaspaceSize=${JVM_MS} -XX:MaxMetaspaceSize=${JVM_MMS} -jar app.jar --spring.profiles.active=$activeEnv"]
#ENTRYPOINT ["sh","-c","java -jar app.jar --spring.profiles.active=$activeEnv"]

# syntax=docker/dockerfile:1

#FROM openjdk8:222.b10
#
#WORKDIR /app
#
#COPY .mvn/ .mvn
#COPY mvnw pom.xml ./
#RUN ./mvnw dependency:go-offline
#
#COPY src ./src
#
#CMD ["./mvnw", "spring-boot:run"]




#FROM midpdeve2registry001.azurecr.cn/library/openjdk8:222.b10
#VOLUME /tmp
#ADD card-platform-api.jar app.jar
##RUN bash -c 'touch /app.jar'
#ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-Duser.timezone=Asia/Shanghai","-jar","/app.jar"]
