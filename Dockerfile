
# build image
FROM openjdk:8u222-slim as java-builder

# create working directory
RUN mkdir -p /work/src \
    && mkdir -p /work/gradle \
    && mkdir -p /work/.git

WORKDIR /work

# Source: https://docs.docker.com/v17.12/develop/develop-images/dockerfile_best-practices/
RUN apt-get update && apt-get install -y \
        imagemagick \
    && rm -rf /var/lib/apt/lists/*

# copy files
COPY .git /work/.git
COPY src /work/src
COPY gradle /work/gradle
COPY build.gradle settings.gradle gradlew /work/

# run build
RUN ./gradlew build

# productive image
FROM openjdk:8u222-jre-slim

RUN apt-get update && apt-get install -y \
        imagemagick \
    && rm -rf /var/lib/apt/lists/*

COPY --from=builder /work/build/libs/*.jar /opt/PreviewService.jar

HEALTHCHECK  --interval=10s --timeout=3s CMD wget --quiet --tries=1 --spider http://localhost:4200/status || exit 1

ENTRYPOINT ["java", "-Xmx6g", "-jar","/opt/PreviewService.jar"]
