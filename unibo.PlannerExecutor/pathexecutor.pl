%====================================================================================
% pathexecutor description   
%====================================================================================
context(ctxpathexecutor, "localhost",  "TCP", "8032").
context(ctxbasicrobot, "127.0.0.1",  "TCP", "8020").
 qactor( basicrobot, ctxbasicrobot, "external").
  qactor( pathexec, ctxpathexecutor, "it.unibo.pathexec.Pathexec").
