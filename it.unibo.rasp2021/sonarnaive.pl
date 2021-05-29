%====================================================================================
% sonarnaive description   
%====================================================================================
mqttBroker("192.168.1.45", "1883", "sonar/data").
context(ctxsonarnaive, "localhost",  "TCP", "8068").
 qactor( sonarsimulator, ctxsonarnaive, "sonarSimulator").
  qactor( sonardatasource, ctxsonarnaive, "sonarHCSR04Support2021").
  qactor( sonarnaive, ctxsonarnaive, "it.unibo.sonarnaive.Sonarnaive").
msglogging.
