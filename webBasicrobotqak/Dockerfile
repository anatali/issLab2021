## ----------------------------------------------------------
## Dockerfile for webBasicrobotqak
## This file should be copied on Rasp together with the
## required resources and executed there
## ----------------------------------------------------------
FROM openjdk:12.0.2
##FROM hypriot/rpi-java
EXPOSE 8085
## ON PC
ADD ./build/distributions/webBasicrobotqak-1.0.tar  /
ADD ./build/distributions/webBasicrobotqak-boot-1.0.tar  /
## ON RASP
## ADD ./webBasicrobotqak-1-0.tar  /

## ADD ./build/distributions/webspring.demo-boot-1.0.1.tar  /

WORKDIR /webBasicrobotqak-boot-1.0/bin
COPY ./*.pl ./
COPY ./*.json ./
RUN ls

CMD ["bash", "webBasicrobotqak"]

### gradlew build
#### docker build -t webbasicrobotqak:1.0 .
### run the image to test
### docker run -p 8085:8085 -ti --rm webbasicrobotqak:1.0 /bin/bash //to use the console
### docker-compose -f virtualrobotguistepper.yaml up
