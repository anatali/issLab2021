package unibo.radarSystem22.actors.main;

 
import it.unibo.radarSystem22.domain.utils.DomainSystemConfig;
import unibo.actor22comm.context.EnablerContextForActors;
import unibo.actor22comm.utils.ColorsOut;
import unibo.actor22comm.utils.CommSystemConfig;
import unibo.actor22comm.utils.CommUtils;
import unibo.radarSystem22.actors.*;
 
  

public class DeviceActorsOnRasp {
private EnablerContextForActors ctx;

	public void doJob() {
		ColorsOut.outappl("DeviceActorsOnRasp | Start", ColorsOut.BLUE);
		configure();
		CommUtils.aboutThreads("Before execute - ");
		//CommUtils.waitTheUser();
		execute();
		terminate();
	}
	
	protected void configure() {
		DomainSystemConfig.simulation   = true;			
		DomainSystemConfig.ledGui       = true;			
		DomainSystemConfig.tracing      = true;					
		CommSystemConfig.tracing        = false;
  
		ctx = new EnablerContextForActors( "ctx",ApplData.ctxPort,ApplData.protocol);
		new LedActor( ApplData.ledName );
		new SonarActor( ApplData.sonarName );
 		//Registrazione dei componenti presso il contesto: NO MORE ... 
  	}
	
	protected void execute() {
		ColorsOut.outappl("DeviceActorsOnRasp | execute", ColorsOut.MAGENTA);
		ctx.activate();
	} 

	public void terminate() {
		CommUtils.aboutThreads("Before exit - ");
// 	    CommUtils.delay(5000);
//		System.exit(0);
	}
	
	public static void main( String[] args) {
		CommUtils.aboutThreads("Before start - ");
		new DeviceActorsOnRasp().doJob();
		CommUtils.aboutThreads("Before end - ");
	}

}

/*
 * Threads:
 */
