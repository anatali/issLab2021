%====================================================================================
% sonarresource description   
%====================================================================================
mqttBroker("localhost", "1883", "sonar/data").
context(ctxsonarresource, "localhost",  "TCP", "8028").
 qactor( sonarresource, ctxsonarresource, "it.unibo.sonarresource.Sonarresource").
  qactor( appllogic, ctxsonarresource, "it.unibo.appllogic.Appllogic").
  qactor( sonarmock, ctxsonarresource, "it.unibo.sonarmock.Sonarmock").
  qactor( ledmock, ctxsonarresource, "it.unibo.ledmock.Ledmock").
