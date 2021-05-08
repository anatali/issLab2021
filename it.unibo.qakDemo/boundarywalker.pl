%====================================================================================
% boundarywalker description   
%====================================================================================
mqttBroker("localhost", "1883", "unibo/boundarywalker").
context(ctxboundarywalker, "localhost",  "TCP", "8068").
context(ctxbasicrobot, "192.168.1.22",  "TCP", "8020").
 qactor( basicrobot, ctxbasicrobot, "external").
  qactor( boundarywalker, ctxboundarywalker, "it.unibo.boundarywalker.Boundarywalker").
