%====================================================================================
% callersealone description   
%====================================================================================
mqttBroker("broker.hivemq.com", "1883", "resource/input").
context(ctxcallers, "localhost",  "TCP", "8049").
context(ctxresource, "127.0.0.1",  "TCP", "8048").
 qactor( resource, ctxresource, "external").
  qactor( caller1, ctxcallers, "it.unibo.caller1.Caller1").
  qactor( caller2, ctxcallers, "it.unibo.caller2.Caller2").
msglogging.
