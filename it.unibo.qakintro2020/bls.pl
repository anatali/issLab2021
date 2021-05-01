%====================================================================================
% bls description   
%====================================================================================
context(ctxbls, "localhost",  "TCP", "8075").
 qactor( button, ctxbls, "it.unibo.button.Button").
  qactor( blscontrol, ctxbls, "it.unibo.blscontrol.Blscontrol").
  qactor( led, ctxbls, "it.unibo.led.Led").
