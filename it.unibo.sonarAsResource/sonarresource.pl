%====================================================================================
% sonarresource description   
%====================================================================================
mqttBroker("broker.hivemq.com", "1883", "sonar/data").
context(ctxsonarresource, "localhost",  "TCP", "8028").
 qactor( sonarresource, ctxsonarresource, "it.unibo.sonarresource.Sonarresource").
tracing.
