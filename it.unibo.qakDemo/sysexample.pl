%====================================================================================
% sysexample description   
%====================================================================================
context(ctxsysexample, "localhost",  "TCP", "8072").
 qactor( qa0, ctxsysexample, "it.unibo.qa0.Qa0").
  qactor( qacoded, ctxsysexample, "kotlinCode.qacoded").
