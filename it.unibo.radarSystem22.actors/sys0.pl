%====================================================================================
% sys0 description   
%====================================================================================
context(ctxsys0, "localhost",  "TCP", "8027").
qactor( led, ctxsys0, "it.unibo.radarSystem22.actors.domain.LedMockActor").
qactor( sonar, ctxsys0, "it.unibo.radarSystem22.actors.domain.SonarMockActor").
qactor( radar, ctxsys0, "it.unibo.radarSystem22.actors.domain.RadarDisplayActor").
qactor( control, ctxsys0, "it.unibo.radarSystem22.actors.experiment22.ControllerActor22"). 
tracing.