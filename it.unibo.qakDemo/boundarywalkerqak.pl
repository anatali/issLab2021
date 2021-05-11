%====================================================================================
% boundarywalkerqak description   
%====================================================================================
context(ctxboundarywalker, "localhost",  "TCP", "8032").
 qactor( boundarywalker, ctxboundarywalker, "it.unibo.boundarywalker.Boundarywalker").
  qactor( testboundary, ctxboundarywalker, "it.unibo.testboundary.Testboundary").
msglogging.
