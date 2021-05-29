%====================================================================================
% sonaronrasp description   
%====================================================================================
mqttBroker("192.168.1.45", "1883", "sonar/data").
context(ctxsonaronrasp, "localhost",  "TCP", "8068").
 qactor( sonarsimulator, ctxsonaronrasp, "sonarSimulator").
  qactor( sonardatasource, ctxsonaronrasp, "sonarHCSR04Support2021").
  qactor( datacleaner, ctxsonaronrasp, "dataCleaner").
  qactor( sonar, ctxsonaronrasp, "it.unibo.sonar.Sonar").
tracing.
