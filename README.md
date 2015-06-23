# Content API Server

## Overview
Rest Application that provides content store in mongodb.
 
## Requierments
- Docker 1.7
- Java

## Build and Run docker image

In order to build and image use:
- ./gradlew buildDocker

if you want to push it
- docker push adolfoecs/content-api-server:0.1.0-SNAPSHOT

In order to run the image use:

- docker run -p 8080 --name content-api-server-instance1 --link mongodb:mongodb -t adolfoecs/content-api-server:0.1.0-SNAPSHOT