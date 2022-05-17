%====================================================================================
% corecaller1 description   
%====================================================================================
context(ctxcorecaller1, "127.0.0.1",  "TCP", "8038").
context(ctxresourcecore, "localhost",  "TCP", "8045").
 qactor( resource, ctxresourcecore, "external").
  qactor( corecaller1, ctxcorecaller1, "it.unibo.corecaller1.Corecaller1").
