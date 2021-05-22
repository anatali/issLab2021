%====================================================================================
% sonarnaive description   
%====================================================================================
mqttBroker("broker.hivemq.com", "1883", "sonar/data").
context(ctxsonar, "localhost",  "TCP", "8068").
 qactor( sonarsimulator, ctxsonar, "sonarSimulator").
  qactor( sonardatasource, ctxsonar, "sonarHCSR04Support2021").
  qactor( datacleaner, ctxsonar, "dataCleaner").
  qactor( sonar, ctxsonar, "it.unibo.sonar.Sonar").
msglogging.
