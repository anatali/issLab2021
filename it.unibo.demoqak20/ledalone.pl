%====================================================================================
% ledalone description   
%====================================================================================
mqttBroker("broker.hivemq.com", "1883", "unibo/ledalone/events").
context(ctxblsledalone, "localhost",  "TCP", "8080").
 qactor( led, ctxblsledalone, "it.unibo.led.Led").
  qactor( manager, ctxblsledalone, "it.unibo.manager.Manager").
msglogging.
