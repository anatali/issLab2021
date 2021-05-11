%====================================================================================
% demo0 description   
%====================================================================================
context(ctxdemo0, "localhost",  "TCP", "8095").
 qactor( demo, ctxdemo0, "it.unibo.demo.Demo").
  qactor( perceiver, ctxdemo0, "it.unibo.perceiver.Perceiver").
