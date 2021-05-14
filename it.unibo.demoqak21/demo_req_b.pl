%====================================================================================
% demo_req_b description   
%====================================================================================
context(ctxdemoreqb, "localhost",  "TCP", "8014").
 qactor( callerb, ctxdemoreqb, "it.unibo.callerb.Callerb").
  qactor( calledb, ctxdemoreqb, "it.unibo.calledb.Calledb").
msglogging.
