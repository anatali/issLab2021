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
 
  
@ActorLocal(
		name =      {"led", "sonar", "controller" }, 
		implement = { LedActor.class,  SonarActor.class,  ControllerActor.class})
public class AllOnPc {
private EnablerContextForActors ctx;

	public void doJob() {
		ColorsOut.outappl("AllOnPc | Start", ColorsOut.BLUE);
		configure();
		CommUtils.aboutThreads("Before execute - ");
		//CommUtils.waitTheUser();
		execute();
		terminate();
	}
	
	protected void configure() {
		DomainSystemConfig.simulation   = true;			
		DomainSystemConfig.ledGui       = true;			
		DomainSystemConfig.tracing      = false;					
		CommSystemConfig.tracing        = false;
  
		//new EventObserver(ApplData.observerName);
		Qak22Context.registerAsEventObserver(ApplData.controllerName, ApplData.evDistance);

		ctx = new EnablerContextForActors( "ctx",ApplData.ctxPcPort,ApplData.protocol);

		Qak22Context.handleLocalActorDecl(this);
		Qak22Util.sendAMsg(ApplData.activateSonar);
//		new LedActor( ApplData.ledName );
//		new SonarActor( ApplData.sonarName );
  	}
	
	protected void execute() {
		ColorsOut.outappl("AllOnPc | execute", ColorsOut.MAGENTA);
		ctx.activate();
		
	} 

	public void terminate() {
		CommUtils.aboutThreads("Before exit - ");
// 	    CommUtils.delay(5000);
//		System.exit(0);
	}
	
	public static void main( String[] args) {
		CommUtils.aboutThreads("Before start - ");
		new AllOnPc().doJob();
		CommUtils.aboutThreads("Before end - ");
	}

}

/*
 * Threads:
 */
