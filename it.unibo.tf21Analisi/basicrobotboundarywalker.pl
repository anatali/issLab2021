%====================================================================================
% basicrobotboundarywalker description   
%====================================================================================
context(ctxbasicboundarywalker, "localhost",  "TCP", "8022").
context(ctxbasicrobot, "192.168.1.15",  "TCP", "8020").
 qactor( basicrobot, ctxbasicrobot, "external").
  qactor( basicboundarywalker, ctxbasicboundarywalker, "it.unibo.basicboundarywalker.Basicboundarywalker").
  qactor( testboundary, ctxbasicboundarywalker, "it.unibo.testboundary.Testboundary").
tracing.
