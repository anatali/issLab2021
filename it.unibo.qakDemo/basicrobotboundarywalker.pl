%====================================================================================
% basicrobotboundarywalker description   
%====================================================================================
context(ctxbundarywalker, "localhost",  "TCP", "8022").
context(ctxbasicrobot, "127.0.0.1",  "TCP", "8020").
 qactor( basicrobot, ctxbasicrobot, "external").
  qactor( basicboundarywalker, ctxbundarywalker, "it.unibo.basicboundarywalker.Basicboundarywalker").
  qactor( envsonarhandler, ctxbundarywalker, "it.unibo.envsonarhandler.Envsonarhandler").
  qactor( testboundary, ctxbundarywalker, "it.unibo.testboundary.Testboundary").
