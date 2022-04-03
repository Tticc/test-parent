
# syntax=docker/dockerfile:1
#FROM adoptopenjdk/openjdk8
FROM tticc/openjdk:8-alpine3.15.3
WORKDIR .
COPY ./target/tester-async.jar app.jar
#ENTRYPOINT exec java -jar app.jar --dev
ENTRYPOINT ["sh","-c","java -jar app.jar --spring.profiles.active=dev"]

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
