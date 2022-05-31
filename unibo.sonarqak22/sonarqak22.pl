%====================================================================================
% sonarqak22 description   
%====================================================================================
context(ctxsonarqak22, "localhost",  "TCP", "8080").
 qactor( sonarqak22, ctxsonarqak22, "it.unibo.sonarqak22.Sonarqak22").
  qactor( sonarmastermock, ctxsonarqak22, "it.unibo.sonarmastermock.Sonarmastermock").
