%====================================================================================
% ledobserver description   
%====================================================================================
mqttBroker("broker.hivemq.com", "1883", "unibo/ledalone/events").
context(ctxdummy, "192.168.1.88",  "TCP", "8080").
context(ctxledobserver, "localhost",  "TCP", "8083").
 qactor( ledobserver, ctxledobserver, "it.unibo.ledobserver.Ledobserver").
