%====================================================================================
% caller2 description   
%====================================================================================
mqttBroker("broker.hivemq.com", "1883", "unibo/resource/events").
context(ctxres2, "localhost",  "TCP", "8048").
context(ctxcaller2, "127.0.0.1",  "TCP", "8045").
 qactor( resource, ctxres2, "external").
  qactor( caller2, ctxcaller2, "it.unibo.caller2.Caller2").
msglogging.
