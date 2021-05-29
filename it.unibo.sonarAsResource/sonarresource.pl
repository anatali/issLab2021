%====================================================================================
% sonarresource description   
%====================================================================================
mqttBroker("192.168.1.45", "1883", "sonar/data").
context(ctxsonarresource, "192.168.1.45",  "TCP", "8028").
 qactor( sonarresource, ctxsonarresource, "it.unibo.sonarresource.Sonarresource").
  qactor( sendermock, ctxsonarresource, "it.unibo.sendermock.Sendermock").
