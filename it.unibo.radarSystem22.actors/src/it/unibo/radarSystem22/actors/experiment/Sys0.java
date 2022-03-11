package it.unibo.radarSystem22.actors.experiment;

import it.unibo.actorComm.utils.ColorsOut;
import it.unibo.kactor.Actor22;
import it.unibo.kactor.QakContext;
import it.unibo.kactor.sysUtil;
import it.unibo.radarSystem22.domain.utils.BasicUtils;
import it.unibo.radarSystem22.domain.utils.DomainSystemConfig;
import kotlinx.coroutines.GlobalScope;

public class Sys0 {
	
	public void doJob() {
		ColorsOut.outappl("Sys0 | Start", ColorsOut.BLUE);
		Actor22.createActorSystem("sys0", "localhost", 8027, "sys0.pl");
	    //QakContext.Companion.createContexts("localhost", GlobalScope.INSTANCE, "sys0.pl", "sysRules.pl" );
		BasicUtils.aboutThreads("Before execute - ");
		//BasicUtils.waitTheUser();
		//execute();
	}

	public static void main( String[] args) {
		DomainSystemConfig.tracing      = false;			
		DomainSystemConfig.sonarDelay   = 150;
		//Su PC
		DomainSystemConfig.simulation   = true;
		DomainSystemConfig.DLIMIT      	= 80;  
		DomainSystemConfig.ledGui       = true;
		 
		BasicUtils.aboutThreads("Before start - ");
		new Sys0().doJob();
		if( ! DomainSystemConfig.ledGui ) BasicUtils.delay(5000);   
		BasicUtils.aboutThreads("Before end - ");
	}

}
