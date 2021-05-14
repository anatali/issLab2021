%====================================================================================
% resourcealone description   
%====================================================================================
mqttBroker("broker.hivemq.com", "1883", "resource/events").
context(ctxresource, "127.0.0.1",  "TCP", "8048").
 qactor( resource, ctxresource, "it.unibo.resource.Resource").
tracing.
msglogging.
