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

##  docker run -it --rm --name basicrobot22 -p8020:8020/tcp --privileged basicrobot22:2.0  /bin/bash

# docker cp basicrobotConfig.json a9c36dac5bdd:/unibo.basicrobot-2.0/bin/basicrobotConfig.json

# docker cp stepTimeConfig.json a9c36dac5bdd:/unibo.basicrobot-2.0/bin/basicrobotConfig.json