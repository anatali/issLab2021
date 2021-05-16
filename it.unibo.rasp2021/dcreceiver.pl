%====================================================================================
% dcreceiver description   
%====================================================================================
context(ctxdcreceiver, "localhost",  "TCP", "8037").
 qactor( dcreceiver, ctxdcreceiver, "it.unibo.dcreceiver.Dcreceiver").
