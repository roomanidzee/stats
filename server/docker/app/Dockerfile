FROM openjdk:11.0.5-stretch

LABEL maintainer = 'Andrey Romanov'

COPY . /code
COPY ./docker/app/entrypoint.sh /docker-entrypoint.sh

WORKDIR /code

ARG PROD_JVM_OPTS
ARG PORT
ARG DOCKERIZE_VERSION
ARG SBT_VERSION

ENV JVM_OPTS=${PROD_JVM_OPTS}

RUN \
    curl -L -o sbt-${SBT_VERSION}.deb http://dl.bintray.com/sbt/debian/sbt-${SBT_VERSION}.deb && \
    dpkg -i sbt-${SBT_VERSION}.deb && \
    rm sbt-${SBT_VERSION}.deb && \
    apt-get update && \
    apt-get install sbt && \
    sbt sbtVersion

RUN sbt construct

RUN wget "https://github.com/jwilder/dockerize/releases/download/${DOCKERIZE_VERSION}/dockerize-alpine-linux-amd64-${DOCKERIZE_VERSION}.tar.gz" \
        && tar -C /usr/local/bin -xzvf "dockerize-alpine-linux-amd64-${DOCKERIZE_VERSION}.tar.gz" \
        && rm "dockerize-alpine-linux-amd64-${DOCKERIZE_VERSION}.tar.gz"

EXPOSE ${PORT}

ENTRYPOINT ["/docker-entrypoint.sh"]