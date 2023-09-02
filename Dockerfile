# syntax = docker/dockerfile:1.2
FROM clojure:openjdk-17 AS build

WORKDIR /
COPY . /

RUN clj -Sforce -T:build all

FROM azul/zulu-openjdk-alpine:17

RUN apk --no-cache add curl

COPY --from=build /target/oneaccord-standalone.jar /oneaccord/oneaccord-standalone.jar

EXPOSE $PORT

ENTRYPOINT exec java $JAVA_OPTS -jar /oneaccord/oneaccord-standalone.jar
