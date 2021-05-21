%====================================================================================
% sonarresource description   
%====================================================================================
mqttBroker("broker.hivemq.com", "1883", "unibo/sonarResource").
context(ctxsonarresource, "localhost",  "TCP", "8028").
 qactor( sonarresource, ctxsonarresource, "it.unibo.sonarresource.Sonarresource").
