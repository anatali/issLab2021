%====================================================================================
% basicrobotboundarywalker description   
%====================================================================================
context(ctxbasicboundarywalker, "localhost",  "TCP", "8022").
context(ctxbasicrobot, "127.0.0.1",  "TCP", "8020").
 qactor( basicrobot, ctxbasicrobot, "external").
  qactor( basicboundarywalker, ctxbasicboundarywalker, "it.unibo.basicboundarywalker.Basicboundarywalker").
  qactor( testboundary, ctxbasicboundarywalker, "it.unibo.testboundary.Testboundary").
