%====================================================================================
% sonarnaive description   
%====================================================================================
mqttBroker("broker.hivemq.com", "1883", "unibo/nat/sonar/events").
context(ctxsonar, "localhost",  "TCP", "8068").
 qactor( sonardatasource, ctxsonar, "resources.rasp.sonar.sonarHCSR04Support2021").
  qactor( sonar, ctxsonar, "it.unibo.sonar.Sonar").
msglogging.
