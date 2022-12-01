## ----------------------------------------------------------
## Dockerfile for unibo.mapperQak22
## ----------------------------------------------------------

FROM openjdk:12.0.2
EXPOSE 8032
## ADD extracts the tar
ADD ./build/distributions/it.unibo.mapwithobstqak22-1.0.tar /
WORKDIR /it.unibo.mapwithobstqak22-1.0/bin
COPY ./*.pl ./
COPY ./*.json ./
## COPY ./*.py ./

CMD ["bash", "it.unibo.mapwithobstqak22-1.0"]

## gradlew distTar
##  docker build -t mapperqak22:1.0 .
## OCCORRE APRIRE UDP PER PERMETTERE COAP
## docker run -it --rm --name basicrobot22 -p8020:8020/tcp -p8020:8020/udp --privileged basicrobot22:2.0  /bin/bash
## MODIFICA DEL FILE DI CONFIGURAZIONE
# docker cp basicrobotConfig.json a343be9333cb:/unibo.basicrobot22-2.0/bin/basicrobotConfig.json
# docker cp stepTimeConfig.json a9c36dac5bdd:/unibo.basicrobot-2.0/bin/basicrobotConfig.json
# docker tag mapperqak22:1.0 natbodocker/mapperqak22:1.0

#type docker_password.txt | docker login --username natbodocker --password-stdin
#docker login --username natbodocker --password xyz
#docker push natbodocker/mapperqak22:1.0