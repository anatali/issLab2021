package it.unibo.radarSystem22.actors.simple.main;

import it.unibo.actorComm.ActorJK;
import it.unibo.actorComm.ProtocolType;
import it.unibo.actorComm.annotations.ActorLocal;
import it.unibo.actorComm.utils.ColorsOut;
import it.unibo.actorComm.utils.CommSystemConfig;
import it.unibo.actorComm.utils.CommUtils;
import it.unibo.kactor.IApplMessage;
import it.unibo.radarSystem22.domain.utils.BasicUtils;
import it.unibo.radarSystem22.domain.utils.DomainSystemConfig;
 
 
 
/*
 * Sistema che usa led e controller come attori locali
 */
@ActorLocal(name =     {"controller","led" }, 
           implement = {it.unibo.radarSystem22.actors.businessLogic.ControllerActor.class,
        		        it.unibo.radarSystem22.actors.domain.LedActor.class})
public class ControllerLedOnPc {
//Definizione dei nomi degli attori
	protected final String ledName        = "led";
	protected final String controllerName = "controller";
	
//Definizione dei messaggi
	protected IApplMessage turnOnLed    = CommUtils.buildDispatch(controllerName, "cmd", "turnOn",  ledName);
	protected IApplMessage turnOffLed   = CommUtils.buildDispatch(controllerName, "cmd", "turnOff", ledName);
	protected IApplMessage activateCrtl = CommUtils.buildDispatch("main", "cmd", "activate", controllerName);
  
	public void doJob() {
		ColorsOut.outappl("ControllerLedOnPc | Start", ColorsOut.BLUE);
		configure();
		BasicUtils.aboutThreads("Before execute - ");
		//BasicUtils.waitTheUser();
		execute();
		terminate();
	}
	
	protected void configure() {
		DomainSystemConfig.simulation   = true;			
		DomainSystemConfig.ledGui       = true;			
		DomainSystemConfig.tracing      = false;			
   		ActorJK.handleLocalActorDecl(this);
  	}
	
	protected void execute() {
		ColorsOut.outappl("ControllerLedOnPc | execute", ColorsOut.MAGENTA);
		for( int i=1; i<=2; i++) {
	 	    ActorJK.sendAMsg(turnOnLed, ledName  );
	 	    CommUtils.delay(500);
	 	    ActorJK.sendAMsg(turnOffLed, ledName  );
	 	    CommUtils.delay(500);
		}
 		ActorJK.sendAMsg( activateCrtl );
	} 

	public void terminate() {
 	    CommUtils.delay(1500);
		BasicUtils.aboutThreads("Before exit - ");
		System.exit(0);
	}
	
	public static void main( String[] args) {
		BasicUtils.aboutThreads("Before start - ");
		new ControllerLedOnPc().doJob();
 		BasicUtils.aboutThreads("Before end - ");
	}

}

/*
 * Threads:
 */
