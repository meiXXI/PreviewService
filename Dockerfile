# build webapp
FROM node:18 as webapp-builder

ARG VERSION=0.0.0

COPY ["src/main/webapp", "/home/node/app"]
WORKDIR /home/node/app

RUN yarn install
RUN yarn version --new-version ${VERSION}
RUN yarn build

# create base image
FROM amazoncorretto:17 AS base

ARG COMMIT_SHA
ARG COMMIT_SHORT_SHA
ARG PIPELINE_ID
ARG PIPELINE_IID
ARG USER_NAME

ENV COMMIT_SHA=${COMMIT_SHA}
ENV COMMIT_SHORT_SHA=${COMMIT_SHORT_SHA}
ENV PIPELINE_ID=${PIPELINE_ID}
ENV PIPELINE_IID=${PIPELINE_IID}
ENV USER_NAME=${USER_NAME}

ENV LANG=en_US.utf-8
ENV LC_ALL=en_US.UTF-8

RUN yum -y install ImageMagick shadow-utils.x86_64 \
    && yum clean all \
    && rm -rf /var/cache/yum

RUN groupadd -g 1000 meixxi && useradd -u 1000 -g meixxi -s /bin/sh meixxi

# compile and test
FROM base AS java-builder

RUN mkdir -p /work/.gradle && chown -R meixxi:meixxi /work

COPY --chown=meixxi:meixxi ["src", "/work/src"]
COPY --chown=meixxi:meixxi ["gradle", "/work/gradle"]
COPY --chown=meixxi:meixxi ["gradlew", "build.gradle", "settings.gradle", "/work/"]

COPY --chown=meixxi:meixxi --from=webapp-builder ["/home/node/app/build", "/work/src/main/resources/static"]

USER meixxi

WORKDIR /work

ARG VERSION=0.0.0
ARG AWS_ACCESS_KEY_ID
ARG AWS_SECRET_ACCESS_KEY
ARG AWS_SESSION_TOKEN

RUN ./gradlew -i build -PprojectVersion=${VERSION} --no-daemon
RUN rm /work/build/libs/*-plain.jar

# create final image
FROM base

RUN mkdir -p /app/data && chown -R meixxi:meixxi /app
COPY --from=java-builder --chown=meixxi:meixxi ["/work/build/libs/*.jar", "/app/preview-service.jar"]

USER meixxi

ENTRYPOINT ["java", "-Xmx4g", "-jar", "-Dspring.profiles.active=prod", "/app/preview-service.jar"]

EXPOSE 8080
