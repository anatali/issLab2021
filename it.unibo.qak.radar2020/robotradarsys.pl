%====================================================================================
% robotradarsys description   
%====================================================================================
mqttBroker("localhost", "1883", "unibo/radar").
context(ctxradargui, "localhost",  "TCP", "8038").
 qactor( radargui, ctxradargui, "it.unibo.radargui.Radargui").
