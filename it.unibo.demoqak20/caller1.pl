%====================================================================================
% caller1 description   
%====================================================================================
mqttBroker("broker.hivemq.com", "1883", "unibo/resource/events").
context(ctxres1, "localhost",  "TCP", "8048").
context(ctxcaller1, "127.0.0.1",  "TCP", "8043").
 qactor( resource, ctxres1, "external").
  qactor( caller1, ctxcaller1, "it.unibo.caller1.Caller1").
msglogging.
