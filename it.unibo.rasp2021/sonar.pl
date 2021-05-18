%====================================================================================
% sonar description   
%====================================================================================
mqttBroker("broker.hivemq.com", "1883", "unibo/nat/sonar/events").
context(ctxsonar, "localhost",  "TCP", "8068").
 qactor( sonarsimulator, ctxsonar, "rx.sonarSimulator").
  qactor( sonardatasource, ctxsonar, "sensors.sonarHCSR04SupportActor").
  qactor( datalogger, ctxsonar, "rx.dataLogger").
  qactor( datacleaner, ctxsonar, "rx.dataCleaner").
  qactor( distancefilter, ctxsonar, "rx.distanceFilter").
  qactor( sonar, ctxsonar, "it.unibo.sonar.Sonar").
msglogging.
