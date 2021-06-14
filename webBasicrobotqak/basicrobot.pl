%====================================================================================
% basicrobot description   
%====================================================================================
context(ctxbasicrobot, "localhost",  "TCP", "8020").
  qactor( basicrobotproxy, ctxbasicrobot, "it.unibo.webBasicrobotqak.basicrobotProxy").  %%ADDED
  qactor( datacleaner, ctxbasicrobot, "rx.dataCleaner").
  qactor( distancefilter, ctxbasicrobot, "rx.distanceFilter").
  qactor( basicrobot, ctxbasicrobot, "it.unibo.basicrobot.Basicrobot").
tracing.