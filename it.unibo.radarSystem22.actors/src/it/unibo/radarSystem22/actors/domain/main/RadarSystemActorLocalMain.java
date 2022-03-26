package it.unibo.radarSystem22.actors.domain.main;

import it.unibo.actorComm.utils.ColorsOut;
import it.unibo.actorComm.utils.CommSystemConfig;
import it.unibo.actorComm.utils.CommUtils;
import it.unibo.kactor.Actor22;
import it.unibo.kactor.Actor22;
import it.unibo.radarSystem22.actors.businessLogic.ControllerActorForLocal;
import it.unibo.radarSystem22.actors.domain.support.DeviceActorFactory;
import it.unibo.radarSystem22.actors.domain.support.DomainData;
import it.unibo.radarSystem22.domain.interfaces.IRadarDisplay;
import it.unibo.radarSystem22.domain.utils.BasicUtils;
import it.unibo.radarSystem22.domain.utils.DomainSystemConfig;
 
 
/*
 * Questo sistema NON usa la infrastruttura Qak per gli attori
 * ma solo Actor22
 */
public class RadarSystemActorLocalMain {
	
	private Actor22 led ;
	private Actor22 sonar ;
	private Actor22 radar;
	private Actor22 controller;
	
 
	public void doJob() {
		CommSystemConfig.tracing      = true;
 		
		ColorsOut.outappl("RadarSystemActorLocalMain | Start", ColorsOut.BLUE);
		configure();
		BasicUtils.aboutThreads("Before execute - ");
		//BasicUtils.waitTheUser();
		execute();
	}
	
	
	protected void configure() {
		led        = DeviceActorFactory.createLed(DomainData.ledName);
		//for( int i=1; i<=3; i++ ) { DeviceActorFactory.createLed("led"+i); }
		sonar      = DeviceActorFactory.createSonar(DomainData.sonarName);
		radar      = DeviceActorFactory.createRadarActor();
		controller = new ControllerActorForLocal(DomainData.controllerName, led, sonar,(Actor22)radar);
			
	}
	
	protected void execute() {
// 		Actor22.sendAMsg( DomainData.ledOn, led);
//		CommUtils.delay(1000);
//		Actor22.sendAMsg( DomainData.ledOff, led);
//		CommUtils.delay(1000);
//		Actor22.sendAMsg( DomainData.sonarActivate, sonar);
//		CommUtils.delay(3000);
//		Actor22.sendAMsg( DomainData.sonarDeactivate, sonar);
		 
		Actor22.sendAMsg( DomainData.controllerActivate, controller);
	} 
	
 	
	public static void main( String[] args) {
		DomainSystemConfig.tracing      = false;			
		DomainSystemConfig.sonarDelay   = 150;
		//Su PC
		DomainSystemConfig.simulation   = true;
		DomainSystemConfig.DLIMIT      	= 80;  
		DomainSystemConfig.ledGui       = true;
 
		BasicUtils.aboutThreads("Before start - ");
		new RadarSystemActorLocalMain().doJob();
		if( ! DomainSystemConfig.ledGui ) BasicUtils.delay(5000);   
		BasicUtils.aboutThreads("Before end - ");
	}
/*
 * Threads  
 *    main		1
 *    sonar		1 (simulator from Domain)
 *    ledgui	1
 *    radar   	2
 *    Actor22
 *    In tutto 6
 */
	
}
