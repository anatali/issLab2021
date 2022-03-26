package it.unibo.radarSystem22.actors.domain.main;

import it.unibo.actorComm.ActorJK;
import it.unibo.actorComm.ProtocolType;
import it.unibo.actorComm.proxy.ProxyAsClient;
import it.unibo.actorComm.utils.ColorsOut;
import it.unibo.actorComm.utils.CommSystemConfig;
import it.unibo.actorComm.utils.CommUtils;
import it.unibo.kactor.Actor22;
import it.unibo.kactor.ActorBasic;
import it.unibo.kactor.IApplMessage;
import it.unibo.radarSystem22.actors.businessLogic.ActionFunction;
import it.unibo.radarSystem22.actors.businessLogic.ControllerActorForLocal;
import it.unibo.radarSystem22.actors.businessLogic.ControllerOnPc;
import it.unibo.radarSystem22.actors.proxy.LedProxyAsClient;
import it.unibo.radarSystem22.actors.proxy.SonarProxyAsClient;
import it.unibo.radarSystem22.actors.domain.support.DeviceActorFactory;
import it.unibo.radarSystem22.actors.domain.support.DomainData;
import it.unibo.radarSystem22.domain.interfaces.*;
import it.unibo.radarSystem22.domain.utils.BasicUtils;
import it.unibo.radarSystem22.domain.utils.DomainSystemConfig;
 
 
 
/*
 * Questo sistema ipotizza che led e sonar siano attori 
 * con cui interagaire  a scambio di messaggi
 */
public class UsingActorsOnPc {
	
//	private ILed led ;
//	private ISonar sonar ;
	private IRadarDisplay radar;
//	private ControllerActorForLocal controller;
	IApplMessage turnOnLed  = CommUtils.buildDispatch("controller", "cmd", "turnOn",  "led");
	IApplMessage turnOffLed = CommUtils.buildDispatch("controller", "cmd", "turnOff", "led");

	IApplMessage sonarActivate   = CommUtils.buildDispatch("controller", "cmd", "activate", "sonar");
	IApplMessage sonarDeactivate = CommUtils.buildDispatch("controller", "cmd", "deactivate", "sonar");
	IApplMessage getDistance     = CommUtils.buildRequest("controller",  "req", "getDistance", "sonar");
	IApplMessage isActive        = CommUtils.buildRequest("controller",  "req", "isActive", "sonar");
	
	
	private String host    = "localhost";
	private String ctxport = ""+RadarSystemConfig.ctxServerPort;
 
	public void doJob() {
		ColorsOut.outappl("UsingActorsOnPc | Start", ColorsOut.BLUE);
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
// 		radar       = DeviceActorFactory.createRadarGui();
		
 	}
	
	protected void execute() {
		ColorsOut.outappl("UsingActorsOnPc | execute", ColorsOut.MAGENTA);
		for( int i=1; i<=2; i++) {
	 	    ActorJK.sendAMsg(turnOnLed, "led"  );

//	 	    boolean b = led.getState();
//	 	    ColorsOut.outappl("UsingActorsOnPc | b ON="+b, ColorsOut.MAGENTA);
	 	    CommUtils.delay(500);
	 	    ActorJK.sendAMsg(turnOffLed, "led"  );
//	 	    b = led.getState();
//	 	    ColorsOut.outappl("UsingActorsOnPc | b OFF="+b, ColorsOut.MAGENTA);
	 	    CommUtils.delay(500);
		}
// 	    ActionFunction endFun = (n) -> { 
// 	    	System.out.println(n); 
// 	    	terminate(); 
// 	    };
// 		controller.start(endFun, 50);
	} 
	
	public void terminate() {
//		sonar.deactivate();
//		((ProxyAsClient) led).close();
//		((ProxyAsClient) sonar).close();
		System.exit(0);
	}
	
	public static void main( String[] args) {
		BasicUtils.aboutThreads("Before start - ");
		new UsingActorsOnPc().doJob();
 		BasicUtils.aboutThreads("Before end - ");
	}
/*
 * Thread per
 */
	
}
