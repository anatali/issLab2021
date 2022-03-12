package it.unibo.radarSystem22.actors.experiment22;

import it.unibo.actorComm.utils.ColorsOut;
import it.unibo.kactor.Actor22;
import it.unibo.radarSystem22.actors.domain.main.RadarSystemConfig;
import it.unibo.radarSystem22.domain.utils.BasicUtils;
import it.unibo.radarSystem22.domain.utils.DomainSystemConfig;


public class Sys0OnRasp {
	
	public void doJob() {
		Actor22.createActorSystem("sys0OnRasp", "127.0.0.1", 8029, "sys0OnRasp.pl");
 		BasicUtils.aboutThreads("After actor system creation - ");
 		Actor22.showActors22();
	}

	public static void main( String[] args) {
		DomainSystemConfig.tracing      = false;			
		DomainSystemConfig.sonarDelay   = 150;
		//Su PC
		DomainSystemConfig.simulation   = true;
		DomainSystemConfig.DLIMIT      	= 80;  
		DomainSystemConfig.ledGui       = DomainSystemConfig.simulation;
		 
		RadarSystemConfig.withRadarGui  = true;
		RadarSystemConfig.DLIMIT      	= 80; 
		BasicUtils.aboutThreads("Before start - ");
		new Sys0OnRasp().doJob();
		if( ! DomainSystemConfig.ledGui ) BasicUtils.delay(600000);   
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
 