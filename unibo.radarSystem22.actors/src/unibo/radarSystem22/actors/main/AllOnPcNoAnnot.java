package unibo.radarSystem22.actors.main;

 
import it.unibo.radarSystem22.domain.utils.DomainSystemConfig;
import unibo.actor22.Qak22Context;
import unibo.actor22.Qak22Util;
import unibo.actor22.annotations.ActorLocal;
import unibo.actor22comm.context.EnablerContextForActors;
import unibo.actor22comm.utils.ColorsOut;
import unibo.actor22comm.utils.CommSystemConfig;
import unibo.actor22comm.utils.CommUtils;
import unibo.radarSystem22.actors.*;
 
  
public class AllOnPcNoAnnot {
private EnablerContextForActors ctx;

	public void doJob() {
		ColorsOut.outappl("AllOnPcNoAnnot | Start", ColorsOut.BLUE);
		configure();
		CommUtils.aboutThreads("Before execute - ");
		//CommUtils.waitTheUser();
		execute();
		terminate();
	}
	
	protected void configure() {
		DomainSystemConfig.simulation   = true;			
		DomainSystemConfig.sonarDelay   = 250;
		DomainSystemConfig.ledGui       = true;			
		DomainSystemConfig.tracing      = false;					
		RadarSystemConfig.DLIMIT        = 65;
		CommSystemConfig.tracing        = true;
		RadarSystemConfig.sonarObservable = false;
		/*
		//Sonar as event emitter
		RadarSystemConfig.sonarObservable = true;
  
		if( RadarSystemConfig.sonarObservable ) {
			//new EventObserver(ApplData.observerName);
			Qak22Context.registerAsEventObserver(ApplData.controllerName, ApplData.evDistance);
		}
		//End Sonar as event emitter
		*/
		ctx = new EnablerContextForActors( "ctx",ApplData.ctxPcPort,ApplData.protocol);

		new LedActor( ApplData.ledName );
		new SonarActor( ApplData.sonarName );
		new ControllerActor( ApplData.controllerName );
 		
		Qak22Util.sendAMsg(ApplData.activateCrtl);
  	}
	
	protected void execute() {
		ColorsOut.outappl("AllOnPcNoAnnot | execute", ColorsOut.MAGENTA);
		ctx.activate();
		
	} 

	public void terminate() {
		CommUtils.aboutThreads("Before exit - ");
// 	    CommUtils.delay(5000);
//		System.exit(0);
	}
	
	public static void main( String[] args) {
		CommUtils.aboutThreads("Before start - ");
		new AllOnPcNoAnnot().doJob();
		CommUtils.aboutThreads("Before end - ");
	}

}

/*
 * Threads:
 */
