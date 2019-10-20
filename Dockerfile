
# build client
FROM node:current-alpine as client-builder

RUN apk add --no-cache git \
    && mkdir /work \
    && chown node:node /work

USER node

COPY --chown=node:node ["src/main/client", "/work/client"]
WORKDIR /work/client
RUN ls -l

RUN npm install
RUN npx ng version
RUN npx ng build --prod=true --outputPath=/work/static --optimization=true


# build java project
FROM openjdk:8-jdk-slim as java-builder

RUN mkdir -p /work/src \
    && mkdir -p /work/gradle \
    && mkdir -p /work/.git

RUN apt-get update && apt-get install -y imagemagick git \
    && rm -rf /var/lib/apt/lists/*

COPY [".git", "/work/.git"]
COPY ["src", "/work/src"]
COPY ["gradle", "/work/gradle"]
COPY ["build.gradle", "settings.gradle", "gradlew", "/work/"]

RUN rm -rf /work/src/main/resources/static
COPY --from=client-builder /work/static /work/src/main/resources/static

WORKDIR /work

RUN ./gradlew build --no-daemon


# build final image
FROM openjdk:8-jre-slim

RUN apt-get update && apt-get install -y imagemagick \
    && rm -rf /var/lib/apt/lists/*

COPY --from=java-builder /work/build/libs/*.jar /opt/PreviewService.jar

# HEALTHCHECK  --interval=10s --timeout=3s CMD wget --quiet --tries=1 --spider http://localhost:8080/status || exit 1

EXPOSE 8080

ENTRYPOINT ["java", "-Xmx4g", "-jar","/opt/PreviewService.jar"]