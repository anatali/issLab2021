package it.unibo.radarSystem22.actors.domain.main;

import it.unibo.actorComm.utils.ColorsOut;
import it.unibo.kactor.ActorBasic;
import it.unibo.kactor.IApplMessage;
import it.unibo.kactor.MsgUtil;
import it.unibo.radarSystem22.actors.domain.ControllerActor;
import it.unibo.radarSystem22.actors.domain.support.DeviceActorFactory;
import it.unibo.radarSystem22.actors.domain.support.DeviceLang;
import it.unibo.radarSystem22.actors.domain.support.DomainMsg;
import it.unibo.radarSystem22.domain.utils.BasicUtils;
import it.unibo.radarSystem22.domain.utils.DomainSystemConfig;
import it.unibo.radarSystem22.actors.context.ActorLocalContext;

public class RadarSystemActorLocalMain {
	
	private ActorBasic led ;
	private ActorBasic sonar ;
	private ActorBasic controller;
	
 
	public void doJob() {
		ColorsOut.outappl("RadarSystemActorLocalMain | Start", ColorsOut.BLUE);
		configure();
		execute();
	}
	
	
	protected void configure() {
		led        = DeviceActorFactory.createLed(DomainMsg.ledName);
		//for( int i=1; i<=3; i++ ) { DeviceActorFactory.createLed(ledName+i); }
		sonar      = DeviceActorFactory.createSonar(DomainMsg.sonarName);
		controller = new ControllerActor(DomainMsg.controllerName, led, sonar);
		
		ActorLocalContext.addActor( led );
		ActorLocalContext.addActor( sonar );
		ActorLocalContext.addActor( controller );
		
	}
	
	protected void execute() {
		MsgUtil.sendMsg(DomainMsg.controllerActivate, controller, null); //null è continuation.
	} 
	
 	
	public static void main( String[] args) {
		DomainSystemConfig.tracing      = true;			
		DomainSystemConfig.sonarDelay   = 150;
		//Su PC
		DomainSystemConfig.simulation   = true;
		DomainSystemConfig.DLIMIT      	= 80;  
		DomainSystemConfig.ledGui       = true;
 
		BasicUtils.aboutThreads("Before start - ");
		new RadarSystemActorLocalMain().doJob();
		BasicUtils.aboutThreads("Before end - ");
	}

}
