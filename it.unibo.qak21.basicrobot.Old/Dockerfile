## ----------------------------------------------------------
## Dockerfile for it.unibo.qak21.basicrobot
## To be used for PC
## ----------------------------------------------------------

FROM openjdk:12.0.2
EXPOSE 8020
## ADD extracts the tar
ADD ./build/distributions/it.unibo.qak21.basicrobot-1.0.tar /
WORKDIR /it.unibo.qak21.basicrobot-1.0/bin
COPY ./*.pl ./
COPY ./*.json ./
## COPY ./*.py ./

CMD ["bash", "it.unibo.qak21.basicrobot"]

##  gradle -b build2021.gradle build
##  docker build -t basicrobot21 . 
##  docker run -it --rm --name basicrobot21 -p8020:8020/tcp basicrobot21  /bin/bash
##  docker cp basicrobotConfigForDockeronPC.json CONTAINERID:/it.unibo.qak21.basicrobot-1.0/bin/basicrobotConfig.json
##  docker cp basicrobotConfigForDockeronPC.json 21c2bf1024e2:/it.unibo.qak21.basicrobot-1.0/bin/basicrobotConfig.json
##  SAVE THE CONTAINER
##  docker commit  CONTAINER
##  TAG the image
##  docker tag IMAGEID name
 