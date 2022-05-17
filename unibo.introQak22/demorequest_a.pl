%====================================================================================
% demorequest_a description   
%====================================================================================
context(ctxddemorequest_a, "localhost",  "TCP", "8010").
 qactor( callerqa1, ctxddemorequest_a, "it.unibo.callerqa1.Callerqa1").
  qactor( callerqa2, ctxddemorequest_a, "it.unibo.callerqa2.Callerqa2").
  qactor( qacalled, ctxddemorequest_a, "it.unibo.qacalled.Qacalled").
msglogging.
