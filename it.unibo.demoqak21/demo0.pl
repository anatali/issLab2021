%====================================================================================
% demo0 description   
%====================================================================================
mqttBroker("localhost", "1883", "demo0/events").
context(ctxdemo0, "localhost",  "TCP", "8095").
 qactor( demo, ctxdemo0, "it.unibo.demo.Demo").
  qactor( sender, ctxdemo0, "it.unibo.sender.Sender").
  qactor( perceiver, ctxdemo0, "it.unibo.perceiver.Perceiver").
