##FROM java:8
FROM openjdk:12.0.2

ADD ./build/distributions/it.unibo.qakradar2021-1.0.tar  /

WORKDIR /it.unibo.qakradar2021-1.0/bin

COPY ./*.pl ./
RUN ls

CMD ["bash", "app"]

### gradelw distTar
#### docker build -t radargui2021:1.0 .
### run the image to test
### docker run -p 8038:8038 -ti --rm radargui2021:1.0 /bin/bash //to use the console

 
### docker-compose -f virtualrobotandstepper.yaml up

