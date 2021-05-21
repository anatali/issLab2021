%====================================================================================
% sonarresource description   
%====================================================================================
mqttBroker("broker.hivemq.com", "1883", "unibo/nat/sonar/events").
context(ctxsonarresource, "localhost",  "TCP", "8028").
 qactor( sonarresource, ctxsonarresource, "it.unibo.sonarresource.Sonarresource").
tracing.
