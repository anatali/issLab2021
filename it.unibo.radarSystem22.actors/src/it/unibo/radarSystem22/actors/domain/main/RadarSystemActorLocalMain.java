package it.unibo.radarSystem22.actors.domain.main;

import it.unibo.actorComm.utils.ColorsOut;
import it.unibo.kactor.ActorBasic;
import it.unibo.kactor.IApplMessage;
import it.unibo.kactor.MsgUtil;
import it.unibo.radarSystem22.actors.domain.DeviceActorFactory;
import it.unibo.radarSystem22.actors.domain.DeviceLang;
import it.unibo.radarSystem22.actors.domain.DomainMsg;
import it.unibo.radarSystem22.domain.utils.BasicUtils;
import it.unibo.radarSystem22.domain.utils.DomainSystemConfig;

public class RadarSystemActorLocalMain {
	
	private ActorBasic led ;
	private ActorBasic sonar ;
	
 
	public void doJob() {
		ColorsOut.outappl("RadarSystemActorLocalMain | Start", ColorsOut.BLUE);
		configure();
		execute();
	}
	
	
	protected void configure() {
		led   = DeviceActorFactory.createLed(DomainMsg.ledName);
		//for( int i=1; i<=3; i++ ) { DeviceActorFactory.createLed(ledName+i); }
		sonar = DeviceActorFactory.createSonar(DomainMsg.sonarName);
	}
	
	protected void execute() {
		
		MsgUtil.sendMsg(DomainMsg.sonarActivate, sonar, null); //null è continuation.
		BasicUtils.delay(1000);
		MsgUtil.sendMsg(DomainMsg.sonarDistance, sonar, null); //null è continuation.
		
//		MsgUtil.sendMsg(ledOn, led, null); //null è continuation.
//		MsgUtil.sendMsg(ledState, led, null); //null è continuation.
//		BasicUtils.delay(500);
//		MsgUtil.sendMsg(ledOff, led, null); //null è continuation.
//		MsgUtil.sendMsg(ledState, led, null); //null è continuation.
		BasicUtils.delay(2000);
		
		MsgUtil.sendMsg(DomainMsg.sonarDeactivate, sonar, null); //null è continuation.
	} 
	
	/*
	 * Il Controller deve essere un actor in grado di ricevere risposte
	 */
	protected void controller() {
		MsgUtil.sendMsg(DomainMsg.sonarActivate, sonar, null); //null è continuation.
		MsgUtil.sendMsg(DomainMsg.sonarDistance, sonar, null); //null è continuation.		
	}
	
	public static void main( String[] args) {
		DomainSystemConfig.tracing      = true;			
		DomainSystemConfig.sonarDelay   = 200;
		//Su PC
		DomainSystemConfig.simulation   = true;
		DomainSystemConfig.DLIMIT      	= 40;  
		DomainSystemConfig.ledGui       = true;
 
		BasicUtils.aboutThreads("Before start - ");
		new RadarSystemActorLocalMain().doJob();
		BasicUtils.aboutThreads("Before end - ");
	}

}
