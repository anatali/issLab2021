##FROM java:8
FROM openjdk:12.0.2

ADD ./app/build/distributions/app-1.0.tar  /

WORKDIR /app-1.0/bin

# COPY ./*.pl ./
RUN ls

CMD ["bash", "app"]




#### docker build -t steprobot:1.0 .
#### docker run -p 8010:8010 -ti --rm steprobot:1.0 /bin/bash
