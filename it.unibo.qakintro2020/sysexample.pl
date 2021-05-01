%====================================================================================
% sysexample description   
%====================================================================================
context(ctxsysexample, "localhost",  "TCP", "8072").
context(ctxbls, "192.168.1.12",  "TCP", "8075").
 qactor( qa0, ctxsysexample, "it.unibo.qa0.Qa0").
  qactor( qacoded, ctxsysexample, "resources.qacoded").
  qactor( led, ctxbls, "external").
