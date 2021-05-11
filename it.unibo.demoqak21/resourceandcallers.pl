%====================================================================================
% resourceandcallers description   
%====================================================================================
context(ctxresourceandco, "localhost",  "TCP", "8048").
 qactor( resourceandco, ctxresourceandco, "it.unibo.resourceandco.Resourceandco").
  qactor( callerco1, ctxresourceandco, "it.unibo.callerco1.Callerco1").
  qactor( callerco2, ctxresourceandco, "it.unibo.callerco2.Callerco2").
msglogging.
