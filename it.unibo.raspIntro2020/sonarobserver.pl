%====================================================================================
% sonarobserver description   
%====================================================================================
mqttBroker("broker.hivemq.com", "1883", "unibo/sonar/events").
context(ctxdummy, "192.168.1.14",  "TCP", "8068").
context(ctxsonarobserver, "localhost",  "TCP", "8083").
 qactor( sonarobserver, ctxsonarobserver, "it.unibo.sonarobserver.Sonarobserver").
