%====================================================================================
% sys0 description   
%====================================================================================
context(ctxsys0, "localhost",  "TCP", "8027").
qactor( led, ctxsys0, "it.unibo.radarSystem22.actors.domain.LedMockActor").
qactor( ledcontrol, ctxsys0, "it.unibo.radarSystem22.actors.experiment.LedControllerActor22"). 
%% tracing.