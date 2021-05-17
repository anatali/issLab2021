%====================================================================================
% blses1_onoff description   
%====================================================================================
context(ctxblses1, "localhost",  "TCP", "8075").
 qactor( blsonoff, ctxblses1, "it.unibo.blsonoff.Blsonoff").
  qactor( led, ctxblses1, "it.unibo.led.Led").
msglogging.
