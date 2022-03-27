package it.unibo.radarSystem22.actors.domain.main;

import it.unibo.actorComm.ActorJK;
import it.unibo.actorComm.ProtocolType;
import it.unibo.actorComm.annotations.ActorLocal;
import it.unibo.actorComm.context.EnablerContextForActors;
import it.unibo.actorComm.utils.CommSystemConfig;
import it.unibo.radarSystem22.domain.utils.BasicUtils;
import it.unibo.radarSystem22.domain.utils.ColorsOut;
import it.unibo.radarSystem22.domain.utils.DomainSystemConfig;
 
/*
 * Questo sistema NON usa la infrastruttura Qak per gli attori
 * ma solo Actor22 e TcpContextServer/proxy per abilitare la distribuzione.
 * Fa coppia con   RadarSystemDistribrOnPc
 */

@ActorLocal(
name = {"led", "sonar"}, 
implement = {it.unibo.radarSystem22.actors.domain.LedActor.class,
	         it.unibo.radarSystem22.actors.domain.SonarActor.class}
)
public class RadarSystemDistribrOnRasp {
	
//	private ActorBasic led ;
//	private ActorBasic sonar ;
	private EnablerContextForActors ctxServer;
	
	public void doJob() {
		ColorsOut.outappl("RadarSystemDistribrOnRasp | Start", ColorsOut.BLUE);
		configure();
		BasicUtils.aboutThreads("Before execute - ");
		//BasicUtils.waitTheUser();
		execute();
	}
	
	 
	protected void configure() {
		DomainSystemConfig.tracing      = false;			
		DomainSystemConfig.sonarDelay   = 150;
		CommSystemConfig.tracing        = false;
		//Su PC
		DomainSystemConfig.simulation   = true;
		DomainSystemConfig.DLIMIT      	= 80;  
		DomainSystemConfig.ledGui       = true;
		ProtocolType protocol = ProtocolType.udp;
		
//		led        = DeviceActorFactory.createLed(DomainData.ledName);
// 		sonar      = DeviceActorFactory.createSonar(DomainData.sonarName);

		ActorJK.handleLocalActorDecl(this);
		ctxServer  = new EnablerContextForActors("CtxServer",RadarSystemConfig.ctxServerPort,protocol);
		//Registrazione dei componenti presso il contesto: NO MORE ...
	}
	
	protected void execute() {
		ctxServer.activate();
 	} 
	
 	
	public static void main( String[] args) {
 
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
