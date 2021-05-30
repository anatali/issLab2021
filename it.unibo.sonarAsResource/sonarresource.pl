%====================================================================================
% sonarresource description   
%====================================================================================
context(ctxsonarresource, "localhost",  "TCP", "8028").
 qactor( sonarresource, ctxsonarresource, "it.unibo.sonarresource.Sonarresource").
  qactor( appllogic, ctxsonarresource, "it.unibo.appllogic.Appllogic").
  qactor( sendermock, ctxsonarresource, "it.unibo.sendermock.Sendermock").
