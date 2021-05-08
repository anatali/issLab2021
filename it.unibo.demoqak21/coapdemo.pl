%====================================================================================
% coapdemo description   
%====================================================================================
context(ctxcoapdemo, "localhost",  "TCP", "8037").
 qactor( actorcoap, ctxcoapdemo, "it.unibo.actorcoap.Actorcoap").
msglogging.
