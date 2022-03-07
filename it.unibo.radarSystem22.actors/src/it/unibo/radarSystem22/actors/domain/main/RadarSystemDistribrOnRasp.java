package it.unibo.radarSystem22.actors.domain.main;

import it.unibo.actorComm.MsgHandlerForActor;
import it.unibo.actorComm.context.TcpContextServer;
import it.unibo.actorComm.interfaces.IApplMsgHandler;
import it.unibo.actorComm.utils.CommSystemConfig;
import it.unibo.kactor.ActorBasic;
import it.unibo.kactor.MsgUtil;
import it.unibo.radarSystem22.actors.domain.ControllerActor;
import it.unibo.radarSystem22.actors.domain.support.DeviceActorFactory;
import it.unibo.radarSystem22.actors.domain.support.DeviceLang;
import it.unibo.radarSystem22.actors.domain.support.DomainData;
 
import it.unibo.radarSystem22.domain.utils.BasicUtils;
import it.unibo.radarSystem22.domain.utils.ColorsOut;
import it.unibo.radarSystem22.domain.utils.DomainSystemConfig;
 

public class RadarSystemDistribrOnRasp {
	
	private ActorBasic led ;
	private ActorBasic sonar ;
 	private TcpContextServer contextServer;
 
	public void doJob() {
		ColorsOut.outappl("RadarSystemDistribrOnRasp | Start", ColorsOut.BLUE);
		configure();
		BasicUtils.aboutThreads("Before execute - ");
		//BasicUtils.waitTheUser();
		execute();
	}
	
	
	protected void configure() {
		led        = DeviceActorFactory.createLed(DomainData.ledName);
 		sonar      = DeviceActorFactory.createSonar(DomainData.sonarName);
 	    contextServer  = new TcpContextServer("TcpCtxServer",RadarSystemConfig.ctxServerPort);
		//Registrazione dei componenti presso il contesto
		 IApplMsgHandler ledHandler   = new MsgHandlerForActor( led ); 
		 IApplMsgHandler sonarHandler = new MsgHandlerForActor( sonar ); 
		 contextServer.addComponent(led.getName(),   ledHandler);
		 contextServer.addComponent(sonar.getName(), sonarHandler);
			
	}
	
	protected void execute() {
		contextServer.activate();
 	} 
	
 	
	public static void main( String[] args) {
		CommSystemConfig.tracing        = true;
		DomainSystemConfig.tracing      = false;			
		DomainSystemConfig.sonarDelay   = 150;
		//Su PC
		DomainSystemConfig.simulation   = true;
		DomainSystemConfig.DLIMIT      	= 80;  
		DomainSystemConfig.ledGui       = true;
 
		BasicUtils.aboutThreads("Before start - ");
		new RadarSystemDistribrOnRasp().doJob();
		if( ! DomainSystemConfig.ledGui ) BasicUtils.delay(5000);   
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
