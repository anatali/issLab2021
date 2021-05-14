%====================================================================================
% resourcealone description   
%====================================================================================
mqttBroker("broker.hivemq.com", "1883", "resource/input").
context(ctxresource, "127.0.0.1",  "TCP", "8048").
context(ctxcallers, "localhost",  "TCP", "8049").
 qactor( resource, ctxresource, "it.unibo.resource.Resource").
msglogging.
