## -------------------------------------------------------
## Dockerfile for  basicrobot
## -------------------------------------------------------
FROM openjdk:12.0.2
EXPOSE 8020
ADD ./build/distributions/*.tar /
WORKDIR /it.unibo.qak20.basicrobot-1.0/bin
COPY ./*.pl ./
COPY ./*.json ./
CMD ["bash", "it.unibo.qak20.basicrobot"]

## gradle -b build_ctxBasicrobot.gradle distTar
## docker build --rm -t basicrobotqak20 .
## docker run -it --name br20  -p8020:8020/tcp -p8020:8020/udp  basicrobotqak20  /bin/bash
## docker run -ti -p8020:8020/tcp -p8020:8020/udp  br20
## docker restart br20
## docker exec -ti br20 /bin/bash



## Docker should be running a process in the foreground in your container and
## it will be spawned as PID 1 within the container's pid namespace
## there are no other OS processes and daemons running inside the container
## (like systemd, cron, syslog, etc)

## apt-get install iputils-ping -y
## apt-get install net-tools -y
## apt  install openssh-server  systemctl enable ssh  ssh  anatali@192.168.1.22
## docker run -d --name redis --privileged -v /sys/fs/cgroup:/sys/fs/cgroup:ro jrei/systemd-ubuntu:18.04