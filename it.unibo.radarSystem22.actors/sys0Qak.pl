%====================================================================================
% sys0 description   
%====================================================================================
context(ctxsys0qak, "localhost",  "TCP", "8027").
qactor( led, ctxsys0qak, "it.unibo.radarSystem22.actors.domain.LedMockActor").
qactor( ledcontrol, ctxsys0qak, "it.unibo.radarSystem22.actors.experiment.LedControllerActorQak"). 
tracing.