%====================================================================================
% democodedqactor description   
%====================================================================================
context(ctxdemocodedqactor, "localhost",  "TCP", "8065").
 qactor( sonargen, ctxdemocodedqactor, "codedActor.sonarDataGen").
  qactor( datahandler, ctxdemocodedqactor, "it.unibo.datahandler.Datahandler").
