package it.unibo.radarSystem22.actors.simple.distrib.main;

 
import it.unibo.actorComm.ProtocolType;
import it.unibo.actorComm.utils.ColorsOut;
import it.unibo.actorComm.utils.CommSystemConfig;
import it.unibo.radarSystem22.actors.domain.main.RadarSystemConfig;
import it.unibo.radarSystem22.domain.utils.BasicUtils;
import it.unibo.radarSystem22.domain.utils.DomainSystemConfig;
import it.unibo.actorComm.context.EnablerContextForActors; 
import it.unibo.radarSystem22.actors.domain.support.DeviceActorFactory;
import it.unibo.radarSystem22.actors.simple.main.ApplData; 
 
/*
 * Sistema che usa led e controller come attori locali
 */
 
public class LedActorOnRasp {
private EnablerContextForActors ctx;

	public void doJob() {
		ColorsOut.outappl("LedActorOnRasp | Start", ColorsOut.BLUE);
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
		
//		RadarSystemConfig.ctxServerPort = ApplData.ctxPort;
//		ProtocolType protocol           = ProtocolType.tcp;

		ctx = new EnablerContextForActors( "ctx",ApplData.ctxPort,ApplData.protocol);
		DeviceActorFactory.createLed(ApplData.ledName);
 		//new LedActor(ApplData.ledName);
 		//Registrazione dei componenti presso il contesto: NO MORE ... 
  	}
	
	protected void execute() {
		ColorsOut.outappl("LedActorOnRasp | execute", ColorsOut.MAGENTA);
		ctx.activate();
	} 

	public void terminate() {
		BasicUtils.aboutThreads("Before exit - ");
// 	    CommUtils.delay(5000);
//		System.exit(0);
	}
	
	public static void main( String[] args) {
		BasicUtils.aboutThreads("Before start - ");
		new LedActorOnRasp().doJob();
 		BasicUtils.aboutThreads("Before end - ");
	}

}

/*
 * Threads:
 */
