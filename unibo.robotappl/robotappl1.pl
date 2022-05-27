%====================================================================================
% robotappl1 description   
%====================================================================================
context(ctxpathexecutor, "127.0.0.1",  "TCP", "8062").
context(ctxrobotappl, "localhost",  "TCP", "8078").
 qactor( pathexec, ctxpathexecutor, "external").
  qactor( robotappl1, ctxrobotappl, "it.unibo.robotappl1.Robotappl1").
