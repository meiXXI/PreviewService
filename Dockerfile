# build webapp
FROM node:18 as webapp-builder

ARG VERSION=0.0.0

COPY ["src/main/webapp", "/home/node/app"]
WORKDIR /home/node/app

RUN yarn install
RUN yarn version --new-version ${VERSION}
RUN yarn build

# build java application
FROM eclipse-temurin:17-alpine as java-build

ARG VERSION=0.0.0

RUN addgroup -S meixxi && adduser -S meixxi -G meixxi

RUN mkdir -p /work/.gradle && chown -R meixxi:meixxi /work

COPY --chown=meixxi:meixxi ["src", "/work/src"]
COPY --chown=meixxi:meixxi ["gradle", "/work/gradle"]
COPY --chown=meixxi:meixxi ["gradlew", "build.gradle", "settings.gradle", "/work/"]

COPY --chown=meixxi:meixxi --from=webapp-builder ["/home/node/app/build", "/work/src/main/resources/static"]

USER meixxi

WORKDIR /work

RUN ./gradlew -i build -PprojectVersion=${VERSION} --no-daemon
RUN rm /work/build/libs/*-plain.jar

# build JRE
FROM java-build as jre-build

 RUN jlink --verbose \
         --add-modules ALL-MODULE-PATH \
         --compress 2 \
         --strip-java-debug-attributes \
         --no-header-files \
         --no-man-pages \
         --output /work/jre

# create final image
FROM alpine:3

LABEL org.opencontainers.image.source=https://github.com/meixxi/preview-service
LABEL org.opencontainers.image.description="Preview Generation Service"
LABEL org.opencontainers.image.licenses="Apache License 2.0"

ARG GITHUB_RUN_ID
ARG GITHUB_RUN_NUMBER
ARG GITHUB_SHORT_SHA
ARG GITHUB_SHA
ARG GITHUB_ACTOR
ARG VERSION

ENV GITHUB_RUN_ID=${GITHUB_RUN_ID}
ENV GITHUB_RUN_NUMBER=${GITHUB_RUN_NUMBER}
ENV GITHUB_SHORT_SHA=${GITHUB_SHORT_SHA}
ENV GITHUB_SHA=${GITHUB_SHA}
ENV GITHUB_ACTOR=${GITHUB_ACTOR}

ENV VERSION=${VERSION}

ENV LANG=en_US.utf-8
ENV LC_ALL=en_US.UTF-8

ENV JAVA_HOME=/opt/java/openjdk
ENV PATH "${JAVA_HOME}/bin:${PATH}"

COPY --from=jre-build /work/jre $JAVA_HOME

RUN addgroup -S meixxi && adduser -S meixxi -G meixxi

RUN mkdir -p /opt/app && chown -R meixxi:meixxi /opt/app
COPY --from=java-build --chown=meixxi:meixxi ["/work/build/libs/*.jar", "/opt/app/preview-service.jar"]

USER meixxi

ENTRYPOINT ["java", "-Xmx4g", "-jar", "/opt/app/preview-service.jar"]

EXPOSE 8080