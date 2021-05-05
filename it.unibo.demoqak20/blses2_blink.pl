%====================================================================================
% blses2_blink description   
%====================================================================================
context(ctxblses2, "localhost",  "TCP", "8075").
 qactor( blsblink, ctxblses2, "it.unibo.blsblink.Blsblink").
  qactor( led, ctxblses2, "it.unibo.led.Led").
msglogging.
