package it.unibo.radarSystem22.actors.experiment22;

import it.unibo.actorComm.utils.ColorsOut;
import it.unibo.kactor.Actor22;
import it.unibo.radarSystem22.actors.domain.main.RadarSystemConfig;
import it.unibo.radarSystem22.domain.utils.BasicUtils;
import it.unibo.radarSystem22.domain.utils.DomainSystemConfig;


public class Sys0 {
	
	public void doJob() {
		//ColorsOut.outappl("Sys0 | Start", ColorsOut.BLUE);
		Actor22.createActorSystem("sys0", "localhost", 8027, "sys0.pl");
 		BasicUtils.aboutThreads("After actor system creation - ");
 		Actor22.showActors22();
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
		 
		RadarSystemConfig.withRadarGui  = true;
		RadarSystemConfig.DLIMIT      	= 80; 
		BasicUtils.aboutThreads("Before start - ");
		new Sys0().doJob();
		if( ! DomainSystemConfig.ledGui ) BasicUtils.delay(5000);   
		BasicUtils.aboutThreads("Before end - ");
	}

}

/*
 * THREADS
 * 	Piattaforma Qak senza Coap : 9    (CON Coap: 35)
 *  Actor22  1
 *  LedGui 1
 *  RadarGui 1
 *  
 *  After actor system creation -  curthread=main n=13
 *  Sonar simulato 1
 *  
 *  Il sistema gira con 14 Thread
 */
 