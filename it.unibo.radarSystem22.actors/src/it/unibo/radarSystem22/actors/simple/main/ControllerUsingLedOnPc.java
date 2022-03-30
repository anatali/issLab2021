package it.unibo.radarSystem22.actors.simple.main;

import it.unibo.actorComm.ActorJK;
import it.unibo.actorComm.utils.ColorsOut;
import it.unibo.actorComm.utils.CommSystemConfig;
import it.unibo.actorComm.utils.CommUtils;
import it.unibo.kactor.IApplMessage;
import it.unibo.radarSystem22.actors.domain.LedActor;
import it.unibo.radarSystem22.domain.utils.BasicUtils;
import it.unibo.radarSystem22.domain.utils.DomainSystemConfig;
 
 
 
/*
 * Sistema che usa led e controller come attori locali
 */
 
public class ControllerUsingLedOnPc {
   
	public void doJob() {
		ColorsOut.outappl("ControllerUsingLedOnPc | Start", ColorsOut.BLUE);
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
		CommSystemConfig.tracing        = false;

		new LedActor("led");
		new ControllerActorForLed( ApplData.controllerName );
  	}
	
	protected void execute() {
		ColorsOut.outappl("ControllerUsingLedOnPc | execute", ColorsOut.MAGENTA);
  		ActorJK.sendAMsg( ApplData.activateCrtl );
	} 

	public void terminate() {
		BasicUtils.aboutThreads("Before exit - ");
// 	    CommUtils.delay(5000);
//		System.exit(0);
	}
	
	public static void main( String[] args) {
		BasicUtils.aboutThreads("Before start - ");
		new ControllerUsingLedOnPc().doJob();
 		BasicUtils.aboutThreads("Before end - ");
	}

}

/*
 * Threads:
 */
