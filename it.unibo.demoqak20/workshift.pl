%====================================================================================
% workshift description   
%====================================================================================
context(ctxworkshift, "localhost",  "TCP", "8055").
 qactor( worker, ctxworkshift, "it.unibo.worker.Worker").
  qactor( sender, ctxworkshift, "it.unibo.sender.Sender").
msglogging.
