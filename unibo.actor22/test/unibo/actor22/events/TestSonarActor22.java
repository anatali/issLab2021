package unibo.actor22.events;

import it.unibo.radarSystem22.domain.utils.DomainSystemConfig;
import unibo.actor22.Qak22Context;
import unibo.actor22.Qak22Util;
import unibo.actor22.annotations.ActorLocal;
import unibo.actor22comm.utils.CommSystemConfig;
import unibo.actor22comm.utils.CommUtils;
import unibo.actor22.common.ApplData;
import unibo.actor22.common.EventObserver;
import unibo.actor22.common.RadarSystemConfig;

@ActorLocal(name =     { ApplData.sonarName }, 
			implement = {SonarActor22.class }
)
public class TestSonarActor22 {
	
	public TestSonarActor22() {
		configure();
	}
	
	protected void configure() {
		DomainSystemConfig.simulation   	= true;			
		DomainSystemConfig.tracing      	= false;		
		DomainSystemConfig.sonarDelay   	= 200;
		CommSystemConfig.tracing        	= true;
		RadarSystemConfig.sonarObservable 	= true;
		
 		new EventObserver(ApplData.observerName);
 		Qak22Context.registerAsEventObserver(ApplData.observerName, ApplData.evDistance);
		
		Qak22Context.handleLocalActorDecl(this);
 	}

	
	
	public void doJob() {
		Qak22Util.sendAMsg( ApplData.activateSonar  );
		CommUtils.delay(3000);
		Qak22Util.sendAMsg( ApplData.deactivateSonar );
		
	}

	public static void main( String[] args) {
		CommUtils.aboutThreads("Before start - ");
		new TestSonarActor22().doJob();
		CommUtils.aboutThreads("Before end - ");
	}
}
