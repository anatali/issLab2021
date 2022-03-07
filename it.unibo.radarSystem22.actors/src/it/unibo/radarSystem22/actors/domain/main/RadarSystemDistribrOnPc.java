package it.unibo.radarSystem22.actors.domain.main;

import it.unibo.actorComm.ProtocolType;
import it.unibo.actorComm.utils.ColorsOut;
import it.unibo.radarSystem22.actors.distributed.LedProxyAsClient;
import it.unibo.radarSystem22.actors.distributed.SonarProxyAsClient;
import it.unibo.radarSystem22.actors.domain.ControllerOnPc;
import it.unibo.radarSystem22.domain.DeviceFactory;
import it.unibo.radarSystem22.domain.ActionFunction;
import it.unibo.radarSystem22.domain.utils.BasicUtils;
import it.unibo.radarSystem22.domain.utils.DomainSystemConfig;
import it.unibo.radarSystem22.interfaces.*;
 
 

public class RadarSystemDistribrOnPc {
	
	private ILed led ;
	private ISonar sonar ;
	private IRadarDisplay radar;
	private ControllerOnPc controller;
	
	private String host    = "localhost";
	private String ctxport = ""+RadarSystemConfig.ctxServerPort;
 
	public void doJob() {
		ColorsOut.outappl("RadarSystemActorLocalMain | Start", ColorsOut.BLUE);
		configure();
		BasicUtils.aboutThreads("Before execute - ");
		//BasicUtils.waitTheUser();
		execute();
	}
	
	
	protected void configure() {
		led    		= new LedProxyAsClient("ledPxy",     host, ctxport, ProtocolType.tcp );
  		sonar  		= new SonarProxyAsClient("sonarPxy", host, ctxport, ProtocolType.tcp );
		radar      = DeviceFactory.createRadarGui();
		controller = ControllerOnPc.create(led, sonar,radar );
			
	}
	
	protected void execute() {
 	    ActionFunction endFun = (n) -> { 
 	    	System.out.println(n); //terminate(); 
 	    };
 	    led.turnOff();
 		controller.start(endFun, 50);
	} 
	
	public void terminate() {
		sonar.deactivate();
		System.exit(0);
	}
	
	public static void main( String[] args) {
		DomainSystemConfig.tracing      = false;			
		DomainSystemConfig.DLIMIT       = 60;
		BasicUtils.aboutThreads("Before start - ");
		new RadarSystemDistribrOnPc().doJob();
 		BasicUtils.aboutThreads("Before end - ");
	}
/*
 * Thread per
 *    main
 *    sonar
 *    ledgui
 *    2 per radar  
 *    Actor22
 */
	
}
