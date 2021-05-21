%====================================================================================
% sonarnaive description   
%====================================================================================
mqttBroker("broker.hivemq.com", "1883", "unibo/nat/sonar/events").
context(ctxsonar, "localhost",  "TCP", "8068").
context(ctxsonarresource, "192.168.1.132",  "TCP", "8028").
 qactor( sonarsimulator, ctxsonar, "sonarSimulator").
  qactor( sonardatasource, ctxsonar, "sonarHCSR04Support2021").
  qactor( datacleaner, ctxsonar, "dataCleaner").
  qactor( sonar, ctxsonar, "it.unibo.sonar.Sonar").
msglogging.
