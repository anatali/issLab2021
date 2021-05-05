%====================================================================================
% externalresource description   
%====================================================================================
mqttBroker("broker.hivemq.com", "1883", "unibo/resource/events").
context(ctxresource, "localhost",  "TCP", "8048").
 qactor( resource, ctxresource, "it.unibo.resource.Resource").
msglogging.
