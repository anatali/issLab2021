%====================================================================================
% sys0OnRasp.pl    
%====================================================================================
context(ctxsys0onrasp, "127.0.0.1",  "TCP", "8029").
qactor( led, ctxsys0onrasp, "it.unibo.radarSystem22.actors.domain.LedMockActor").
qactor( sonar, ctxsys0onrasp, "it.unibo.radarSystem22.actors.domain.SonarMockActor").
context(ctxsys0onpc, "localhost",  "TCP", "8027").
qactor( radar, ctxsys0onpc, "it.unibo.radarSystem22.actors.domain.RadarDisplayActor").
qactor( control, ctxsys0onpc, "it.unibo.radarSystem22.actors.experiment22.ControllerActor22"). 
tracing.