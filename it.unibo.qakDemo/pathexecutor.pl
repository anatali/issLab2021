%====================================================================================
% pathexecutor description   
%====================================================================================
context(ctxpathexecutor, "localhost",  "TCP", "8032").
 qactor( pathexec, ctxpathexecutor, "it.unibo.pathexec.Pathexec").
  qactor( spiralwalker, ctxpathexecutor, "it.unibo.spiralwalker.Spiralwalker").
  qactor( boundarywalker, ctxpathexecutor, "it.unibo.boundarywalker.Boundarywalker").
  qactor( sonarsentinel, ctxpathexecutor, "it.unibo.sonarsentinel.Sonarsentinel").
  qactor( testexec, ctxpathexecutor, "it.unibo.testexec.Testexec").
msglogging.
