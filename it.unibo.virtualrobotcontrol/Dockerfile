FROM node:13-alpine
##FROM hypriot/rpi-java
## issLab2021\it.unibo.virtualrobotcontrol
RUN mkdir -p /home/app
COPY ./app /home/app

# set default dir so that next commands executes in /home/app dir
WORKDIR /home/app

# will execute npm install in /home/app because of WORKDIR
RUN npm install

# no need for /home/app/server.js because of WORKDIR
CMD ["node", "webguiServer.js"]

