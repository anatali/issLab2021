%====================================================================================
% sonarresource description   
%====================================================================================
mqttBroker("192.168.1.43", "1883", "sonar/data").
context(ctxsonarresource, "localhost",  "TCP", "8028").
 qactor( sonarresource, ctxsonarresource, "it.unibo.sonarresource.Sonarresource").
  qactor( sendermock, ctxsonarresource, "it.unibo.sendermock.Sendermock").
