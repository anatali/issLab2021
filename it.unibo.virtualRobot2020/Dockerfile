FROM node:13-alpine
RUN mkdir -p /home/node

COPY ./node/WEnv /home/node/WEnv
COPY ./node/WEnv/WebGLScene /home/node/WEnv/WebGLScene


# set default dir so that next commands executes in it
WORKDIR /home/node/WEnv/WebGLScene
RUN npm install

WORKDIR /home/node/WEnv/server
RUN npm install

WORKDIR /home/node/WEnv/server/src
CMD ["node", "WebpageServer"]

## Tag the image
## docker tag usingvirtualrobot:1.0 natbodocker/usingvirtualrobot:1.0

## Register the image on DockerHub
## docker push natbodocker/usingvirtualrobot:1.0

#### docker build -t virtualrobotdisi:1.0 .
#### docker run -ti -p 8090:8090  -p 8091:8091 --rm virtualrobotdisi:1.0 /bin/sh
#### docker run -d -p 8090:8090 -p 8091:8091 --rm virtualrobotdisi:1.0   (Detach mode)