%====================================================================================
% demo_req_a description   
%====================================================================================
context(ctxcallera, "localhost",  "TCP", "8072").
context(ctxcalleda, "127.0.0.1",  "TCP", "8074").
 qactor( callera1, ctxcallera, "it.unibo.callera1.Callera1").
  qactor( callera2, ctxcallera, "it.unibo.callera2.Callera2").
  qactor( calleda, ctxcalleda, "it.unibo.calleda.Calleda").
msglogging.
