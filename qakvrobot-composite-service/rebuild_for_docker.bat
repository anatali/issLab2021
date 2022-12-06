cd ../it.unibo.virtualRobot2020
echo "building virtualRobot2020"
call gradlew build
cd ../unibo.basicrobot22
call gradlew build
cd ../unibo.mapperQak22
call gradlew build
cd ../qakvrobot-composite-service
call docker-compose -f qakvrobotsys.yaml build