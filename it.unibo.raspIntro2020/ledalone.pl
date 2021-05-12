%====================================================================================
% ledalone description   
%====================================================================================
mqttBroker("broker.hivemq.com", "1883", "unibo/nat/ledalone/events").
context(ctxledalone, "localhost",  "TCP", "8080").
 qactor( led, ctxledalone, "it.unibo.led.Led").
msglogging.
