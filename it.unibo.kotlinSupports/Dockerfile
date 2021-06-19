##FROM java:8
FROM openjdk:12.0.2
##FROM hypriot/rpi-java
## issLab2021\it.unibo.kotlinSupports
ADD ./app/build/distributions/app-2.0.tar  /

WORKDIR /app-2.0/bin

# COPY ./*.pl ./
RUN ls

CMD ["bash", "app"]

### gradelw distTar
#### docker build -t steprobot:2.0 .
### run the image to test
### docker run -p 8010:8010 -ti --rm steprobot:2.0 /bin/bash //to use the console
### docker run -ti -p 8090:8090  -p 8091:8091 --rm virtualrobotdisi:2.0 /bin/sh
### Run the steprobot service together with the wenv
### docker-compose -f virtualrobotandstepper.yaml up

