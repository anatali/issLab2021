%====================================================================================
% demo_req_b description   
%====================================================================================
context(ctxcallerb, "localhost",  "TCP", "8076").
context(ctxcalledb, "127.0.0.1",  "TCP", "8078").
 qactor( callerb, ctxcallerb, "it.unibo.callerb.Callerb").
  qactor( calledb, ctxcalledb, "it.unibo.calledb.Calledb").
msglogging.
