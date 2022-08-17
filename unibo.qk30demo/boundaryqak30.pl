%====================================================================================
% boundaryqak30 description   
%====================================================================================
context(ctxboundaryqak30, "localhost",  "TCP", "8032").
context(ctxbasicrobot, "127.0.0.1",  "TCP", "8020").
context(ctxconsole, "127.0.0.1",  "TCP", "8042").
context(ctxobserver, "127.0.0.1",  "TCP", "8020").
 qactor( basicrobot, ctxbasicrobot, "external").
  qactor( boundaryqak30, ctxboundaryqak30, "it.unibo.boundaryqak30.Boundaryqak30").
  qactor( cmdconsole, ctxconsole, "it.unibo.cmdconsole.Cmdconsole").
  qactor( anobserver, ctxobserver, "it.unibo.anobserver.Anobserver").
  qactor( applobserver, ctxobserver, "it.unibo.applobserver.Applobserver").
