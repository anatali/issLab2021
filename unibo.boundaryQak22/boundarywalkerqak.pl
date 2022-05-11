%====================================================================================
% boundarywalkerqak description   
%====================================================================================
context(ctxboundarywalker, "localhost",  "TCP", "8032").
 qactor( boundarywalker, ctxboundarywalker, "it.unibo.boundarywalker.Boundarywalker").
  qactor( mainwalk, ctxboundarywalker, "it.unibo.mainwalk.Mainwalk").
msglogging.
