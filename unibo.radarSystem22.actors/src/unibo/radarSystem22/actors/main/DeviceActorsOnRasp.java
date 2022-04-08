package unibo.radarSystem22.actors.main;

 
import it.unibo.radarSystem22.domain.utils.DomainSystemConfig;
import unibo.actor22.Qak22Context;
import unibo.actor22.annotations.ActorLocal;
import unibo.actor22.annotations.ActorRemote;
import unibo.actor22comm.context.EnablerContextForActors;
import unibo.actor22comm.interfaces.IContext;
import unibo.actor22comm.utils.ColorsOut;
import unibo.actor22comm.utils.CommSystemConfig;
import unibo.actor22comm.utils.CommUtils;
import unibo.radarSystem22.actors.*;
 
  
@ActorLocal(
		name =      {"led", "sonar"  }, 
		implement = { LedActor.class,  SonarActor22.class }
)

@ActorRemote(name =   {"controller" },   //WARNING: dichiazione da evitare in futuro
		host=    { ApplData.pcAddr }, 
		port=    { ""+ApplData.ctxPcPort }, 
		protocol={ "TCP"   }
)

public class DeviceActorsOnRasp {
 

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
		DomainSystemConfig.tracing      = false;					
		CommSystemConfig.tracing        = false;
		
		//DECIDE se Sonar observable o no
		//VA ATTIIVATO PER PRIMO
		RadarSystemConfig.sonarObservable = true;
		
		Qak22Context.handleLocalActorDecl(this); 
		
		if( RadarSystemConfig.sonarObservable  ) {
			Qak22Context.handleRemoteActorDecl(this);
 			Qak22Context.registerAsEventObserver(ApplData.controllerName, ApplData.evDistance);
		}

		IContext ctx = new EnablerContextForActors( "ctxRasp",ApplData.ctxRaspPort,ApplData.protocol);
		ctx.activate();

	}
	
	protected void execute() {
		ColorsOut.outappl("DeviceActorsOnRasp | execute", ColorsOut.MAGENTA);
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
