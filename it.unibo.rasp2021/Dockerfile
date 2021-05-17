## ----------------------------------------------------------
## Dockerfile for qakled2021
## ----------------------------------------------------------
## see https://iotbytes.wordpress.com/create-your-first-docker-container-for-raspberry-pi-to-blink-an-led/
FROM hypriot/rpi-java
EXPOSE 8075
## ADD extracts the tar
ADD *.tar /
WORKDIR /it.unibo.rasp2021-1.0/bin
COPY ./*.pl ./
COPY ./*.sh ./
COPY ./*.py ./

## RUN apt-get update -y
## RUN apt-get install -y wiringpi
## RUN sudo apt-get install -y python

CMD ["bash", "it.unibo.rasp2021"]

##  docker build -t qakled2021 . 
##  docker run -it --rm --name led2021 -p8075:8075/tcp -p8075:8075/udp --privileged qakled2021  /bin/bash
##  docker run -it --rm --name led2021 -p8075:8075/tcp -p8075:8075/udp --privileged qakled2021  /bin/bash



## https://hub.docker.com/r/arm32v7/python/
## docker run -d -p 80:80 hypriot/rpi-busybox-httpd		##works, but ???