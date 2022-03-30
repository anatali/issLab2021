package it.unibo.radarSystem22.actors.simple.main;

import it.unibo.actorComm.ActorJK;
import it.unibo.actorComm.utils.ColorsOut;
import it.unibo.actorComm.utils.CommSystemConfig;
import it.unibo.actorComm.utils.CommUtils;
import it.unibo.radarSystem22.actors.domain.LedActor;
import it.unibo.radarSystem22.domain.utils.BasicUtils;
import it.unibo.radarSystem22.domain.utils.DomainSystemConfig;
 
 
 
/*
 * Sistema che usa led come attore locale
 */
 public class UsingLedNoControllerOnPc {
 	
 
  
	public void doJob() {
		ColorsOut.outappl("UsingLedNoControllerOnPc | Start", ColorsOut.BLUE);
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
   	}
	
	//Accende-spegne il Led due volte
	protected void execute() {
		ColorsOut.outappl("UsingLedNoControllerOnPc | execute", ColorsOut.MAGENTA);
		for( int i=1; i<=2; i++) {
	 	    ActorJK.sendAMsg(ApplData.turnOnLed, ApplData.ledName  );
	 	    CommUtils.delay(500);
// Inviare una request richiede un attore capace di ricevere la reply	 	    
	 	    ActorJK.sendAMsg(ApplData.getState, ApplData.ledName  );
	 	    CommUtils.delay(500); 	    
	 	    ActorJK.sendAMsg(ApplData.turnOffLed, ApplData.ledName  );
	 	    CommUtils.delay(500);
		}
 	} 

	public void terminate() {
		BasicUtils.aboutThreads("Before exit - ");
		System.exit(0);
	}
	
	public static void main( String[] args) {
		BasicUtils.aboutThreads("Before start - ");
		new UsingLedNoControllerOnPc().doJob();
 		BasicUtils.aboutThreads("Before end - ");
	}

}

/*
 * Threads: main + Actor22 + LedGui
 */
