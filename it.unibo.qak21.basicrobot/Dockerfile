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

##  docker build -t basicrobot21 . 
##  docker cp basicrobotConfigForDockeronPC.json CONTAINERID:/it.unibo.qak21.basicrobot-1.0/bin/basicrobotConfig.json
##  docker cp basicrobotConfigForDockeronPC.json 275f04cc0bf9:/it.unibo.qak21.basicrobot-1.0/bin/basicrobotConfig.json
##  SAVE THE CONTAINER
 
 