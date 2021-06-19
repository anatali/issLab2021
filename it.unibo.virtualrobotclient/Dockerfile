##FROM java:8
## issLab2021\it.unibo.virtualrobotclient
FROM openjdk:12.0.2
##FROM hypriot/rpi-java
ADD ./app/build/distributions/app-1.0.tar  /

WORKDIR /app-1.0/bin

## COPY ./*.pl ./
## RUN ls

CMD ["bash", "app"]


## docker build -t vrobotclient:1.0 .
## docker run  -ti --rm vrobotclient:1.0 /bin/bash