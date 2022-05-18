%====================================================================================
% mapperbuilder description   
%====================================================================================
context(ctxmapperbuilder, "localhost",  "TCP", "8032").
context(ctxbasicrobot, "127.0.0.1",  "TCP", "8020").
 qactor( basicrobot, ctxbasicrobot, "external").
  qactor( mapperbuilder, ctxmapperbuilder, "it.unibo.mapperbuilder.Mapperbuilder").
