%====================================================================================
% sonarnaive description   
%====================================================================================
context(ctxsonar, "localhost",  "TCP", "8068").
context(ctxsonarresource, "127.0.0.1",  "TCP", "8028").
 qactor( sonarsimulator, ctxsonar, "sonarSimulator").
  qactor( sonardatasource, ctxsonar, "sonarHCSR04Support2021").
  qactor( datacleaner, ctxsonar, "dataCleaner").
  qactor( sonarresource, ctxsonarresource, "external").
  qactor( sonar, ctxsonar, "it.unibo.sonar.Sonar").
msglogging.
