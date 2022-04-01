package it.unibo.radarSystem22.actors.simple.distrib.main;

import it.unibo.actorComm.ActorJK;
import it.unibo.actorComm.ProtocolType;
import it.unibo.actorComm.utils.ColorsOut;
import it.unibo.actorComm.utils.CommSystemConfig;
import it.unibo.actorComm.utils.CommUtils;
import it.unibo.radarSystem22.actors.domain.main.RadarSystemConfig;
import it.unibo.radarSystem22.actors.simple.main.ApplData;
import it.unibo.radarSystem22.actors.simple.main.ControllerActorOnPc;
import it.unibo.radarSystem22.domain.utils.BasicUtils;
import it.unibo.radarSystem22.domain.utils.DomainSystemConfig;
 
 
 
/*
 * Sistema che usa led e controller come attori locali
 */
 
public class ControllerOnPcUsingLedRemote {
   
	public void doJob() {
		ColorsOut.outappl("ControllerOnPcUsingLedRemote | Start", ColorsOut.BLUE);
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
		CommSystemConfig.tracing        = true;
		
 		RadarSystemConfig.raspHostAddr  = "localhost";
                 
		ActorJK.setActorAsRemote( 
				ApplData.ledName, ""+ApplData.ctxPort,RadarSystemConfig.raspHostAddr, ApplData.protocol);
		new ControllerActorOnPc( ApplData.controllerName );
  	}
	
	protected void execute() {
		ColorsOut.outappl("ControllerOnPcUsingLedRemote | execute", ColorsOut.MAGENTA);
  		ActorJK.sendAMsg( ApplData.activateCrtl );
	} 

	public void terminate() {
		BasicUtils.aboutThreads("Before exit - ");
  	    CommUtils.delay(5000);
//		System.exit(0);
	}
	
	public static void main( String[] args) {
		BasicUtils.aboutThreads("Before start - ");
		new ControllerOnPcUsingLedRemote().doJob();
 		BasicUtils.aboutThreads("Before end - ");
	}

}

/*
 * Threads:
 */
