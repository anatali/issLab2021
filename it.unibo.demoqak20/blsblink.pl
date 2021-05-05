%====================================================================================
% blsblink description   
%====================================================================================
mqttBroker("broker.hivemq.com", "1883", "unibo/blsblink/events").
context(ctxblsledonlyyyy, "localhost",  "TCP", "8075").
context(ctxblsblink, "127.0.0.1",  "TCP", "8077").
 qactor( led, ctxblsledonlyyyy, "external").
  qactor( blsblink, ctxblsblink, "it.unibo.blsblink.Blsblink").
msglogging.
