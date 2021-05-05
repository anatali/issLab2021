%====================================================================================
% coapdemocaller description   
%====================================================================================
context(ctxcoapdemocaller, "127.0.0.1",  "TCP", "8048").
context(ctxcoapdemoext, "localhost",  "TCP", "8037").
 qactor( actorcoap, ctxcoapdemoext, "external").
  qactor( caller, ctxcoapdemocaller, "it.unibo.caller.Caller").
  qactor( coapobserver, ctxcoapdemocaller, "it.unibo.coapobserver.Coapobserver").
msglogging.
