## ----------------------------------------------------------
## Dockerfile for sonarresource
## ----------------------------------------------------------
## see https://iotbytes.wordpress.com/create-your-first-docker-container-for-raspberry-pi-to-blink-an-led/
FROM openjdk:12.0.2
##FROM hypriot/rpi-java
EXPOSE 8028
## ADD extracts the tar
ADD ./build/distributions/*.tar /
WORKDIR it.unibo.sonarAsResource-1.0/bin
COPY ./*.pl ./

## COPY ./*.sh ./
## COPY ./*.py ./


CMD ["bash", "it.unibo.sonarAsResource-1.0"]

##  docker build -t sonarresource . 
##  docker run -it --rm --name sonarresource -p8028:8028/tcp  sonarresource  /bin/bash



## https://hub.docker.com/r/arm32v7/python/
## docker run -d -p 80:80 hypriot/rpi-busybox-httpd		##works, but ???