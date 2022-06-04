%====================================================================================
% basicrobot22 description   
%====================================================================================
context(ctxbasicrobot22, "localhost",  "TCP", "8020").
 qactor( datacleaner, ctxbasicrobot22, "rx.dataCleaner").
  qactor( distancefilter, ctxbasicrobot22, "rx.distanceFilter").
  qactor( basicrobot22, ctxbasicrobot22, "it.unibo.basicrobot22.Basicrobot22").
  qactor( envsonarhandler, ctxbasicrobot22, "it.unibo.envsonarhandler.Envsonarhandler").
  qactor( pathexec, ctxbasicrobot22, "it.unibo.pathexec.Pathexec").
