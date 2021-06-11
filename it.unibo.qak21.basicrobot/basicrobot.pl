%====================================================================================
% basicrobot description   
%====================================================================================
mqttBroker("localhost", "1883", "unibo/basicrobot").
context(ctxbasicrobot, "localhost",  "TCP", "8020").
 qactor( datacleaner, ctxbasicrobot, "rx.dataCleaner").
  qactor( distancefilter, ctxbasicrobot, "rx.distanceFilter").
  qactor( basicrobot, ctxbasicrobot, "it.unibo.basicrobot.Basicrobot").
tracing.
