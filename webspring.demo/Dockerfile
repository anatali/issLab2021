##FROM java:8
FROM openjdk:12.0.2

ADD ./build/distributions/webspring.demo-1.0.1.tar  /
ADD ./build/distributions/webspring.demo-boot-1.0.1.tar  /

WORKDIR /webspring.demo-boot-1.0.1/bin
# COPY ./*.pl ./
RUN ls

CMD ["bash", "webspring.demo"]

### gradelw distTar
#### docker build -t webspringrobot:1.0.1 .
### run the image to test
### docker run -p 8081:8081 -ti --rm webspringrobot:1.0.1 /bin/bash //to use the console

