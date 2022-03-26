package it.unibo.radarSystem22.actors.domain.main;

import it.unibo.actorComm.ProtocolType;
import it.unibo.actorComm.proxy.ProxyAsClient;
import it.unibo.actorComm.utils.ColorsOut;
import it.unibo.actorComm.utils.CommSystemConfig;
import it.unibo.actorComm.utils.CommUtils;
import it.unibo.radarSystem22.actors.businessLogic.ActionFunction;
import it.unibo.radarSystem22.actors.businessLogic.ControllerOnPc;
import it.unibo.radarSystem22.actors.proxy.LedProxyAsClient;
import it.unibo.radarSystem22.actors.proxy.SonarProxyAsClient;
import it.unibo.radarSystem22.actors.domain.support.DeviceActorFactory;
import it.unibo.radarSystem22.domain.interfaces.*;
import it.unibo.radarSystem22.domain.utils.BasicUtils;
import it.unibo.radarSystem22.domain.utils.DomainSystemConfig;
 
 
 
/*
 * Questo sistema NON usa la infrastruttura Qak per gli attori
 * ma solo Actor22 e TcpContextServer/proxy per abilitare la distribuzione.
 * Fa coppia con RadarSystemDistribrOnRasp  
 */
public class RadarSystemDistribrOnPc {
	
	private ILed led ;
	private ISonar sonar ;
	private IRadarDisplay radar;
	private ControllerOnPc controller;
	
	private String host    = "localhost";
	private String ctxport = ""+RadarSystemConfig.ctxServerPort;
 
	public void doJob() {
		ColorsOut.outappl("RadarSystemDistribrOnPc | Start", ColorsOut.BLUE);
		configure();
		BasicUtils.aboutThreads("Before execute - ");
		//BasicUtils.waitTheUser();
		execute();
	}
	
	
	protected void configure() {
		DomainSystemConfig.tracing      = false;			
 		CommSystemConfig.protcolType    = ProtocolType.tcp;
		CommSystemConfig.tracing        = false;
		ProtocolType protocol 		    = CommSystemConfig.protcolType;
		led    		= new LedProxyAsClient("ledPxy",     host, ctxport, protocol );
  		sonar  		= new SonarProxyAsClient("sonarPxy", host, ctxport, protocol );
		radar       = DeviceActorFactory.createRadarGui();
		controller  = ControllerOnPc.create(led, sonar,radar );
			
	}
	
	protected void execute() {
		ColorsOut.outappl("RadarSystemDistribrOnPc | execute", ColorsOut.MAGENTA);
		for( int i=1; i<=2; i++) {
	 	    led.turnOn();
	 	    boolean b = led.getState();
	 	    ColorsOut.outappl("RadarSystemDistribrOnPc | b ON="+b, ColorsOut.MAGENTA);
	 	    CommUtils.delay(500);
	 	    led.turnOff();
	 	    b = led.getState();
	 	    ColorsOut.outappl("RadarSystemDistribrOnPc | b OFF="+b, ColorsOut.MAGENTA);
	 	    CommUtils.delay(500);
		}
 	    ActionFunction endFun = (n) -> { 
 	    	System.out.println(n); 
 	    	terminate(); 
 	    };
 		controller.start(endFun, 50);
	} 
	
	public void terminate() {
		sonar.deactivate();
		((ProxyAsClient) led).close();
		((ProxyAsClient) sonar).close();
		System.exit(0);
	}
	
	public static void main( String[] args) {
		BasicUtils.aboutThreads("Before start - ");
		new RadarSystemDistribrOnPc().doJob();
 		BasicUtils.aboutThreads("Before end - ");
	}
/*
 * Thread per
 *    main
 *    2 per radar  
 *    Actor22
 */
	
}
