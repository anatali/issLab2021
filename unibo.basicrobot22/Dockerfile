## ----------------------------------------------------------
## Dockerfile for unibo.basicrobot22
## ----------------------------------------------------------

FROM openjdk:12.0.2
EXPOSE 8020
## ADD extracts the tar
ADD ./build/distributions/unibo.basicrobot-2.0.tar /
WORKDIR /unibo.basicrobot-2.0/bin
COPY ./*.pl ./
COPY ./*.json ./
## COPY ./*.py ./

## RUN apt-get update -y
## RUN apt-get install -y wiringpi
## RUN sudo apt-get install -y python

CMD ["bash", "unibo.basicrobot"]


##  docker build -t basicrobot22:2.0 .
<<<<<<< HEAD
##  docker run -it --rm --name basicrobot22:2.0 -p8020:8020/tcp --privileged basicrobot22:1.0  /bin/bash
=======
##  docker run -it --rm --name basicrobot22:2.0 -p8020:8020/tcp --privileged basicrobot22:2.0  /bin/bash

# docker cp basicrobotConfig.json a9c36dac5bdd:/unibo.basicrobot-2.0/bin/basicrobotConfig.json
>>>>>>> 45c5085cfe69d122b43ddc226b3ac2a2b6f7f694
