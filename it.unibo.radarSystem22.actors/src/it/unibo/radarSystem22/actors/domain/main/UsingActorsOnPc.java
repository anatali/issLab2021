package it.unibo.radarSystem22.actors.domain.main;

import it.unibo.actorComm.ActorJK;
import it.unibo.actorComm.ProtocolType;
import it.unibo.actorComm.annotations.ActorLocal;
import it.unibo.actorComm.annotations.ActorRemote;
import it.unibo.actorComm.utils.ColorsOut;
import it.unibo.actorComm.utils.CommSystemConfig;
import it.unibo.actorComm.utils.CommUtils;
import it.unibo.kactor.Actor22;
import it.unibo.kactor.IApplMessage;
import it.unibo.radarSystem22.actors.businessLogic.ControllerActor;
import it.unibo.radarSystem22.actors.domain.support.DeviceActorFactory;
import it.unibo.radarSystem22.domain.utils.BasicUtils;
import it.unibo.radarSystem22.domain.utils.DomainSystemConfig;
 
 
 
/*
 * Questo sistema ipotizza che led e sonar siano attori 
 * con cui interagaire  a scambio di messaggi
 */
@ActorLocal(name = {"controller"}, implement = {it.unibo.radarSystem22.actors.businessLogic.ControllerActor.class})
@ActorRemote(name = {"led","sonar"}, host={"localhost","localhost"}, port={"8048","8048"}, protocol={"UDP","UDP"})
public class UsingActorsOnPc {
	
//	 ILed ISonar ARE NO MORE NECESSARY
	private Actor22 radar;
	IApplMessage turnOnLed  = CommUtils.buildDispatch("controller", "cmd", "turnOn",  "led");
	IApplMessage turnOffLed = CommUtils.buildDispatch("controller", "cmd", "turnOff", "led");

	IApplMessage sonarActivate   = CommUtils.buildDispatch("controller", "cmd", "activate", "sonar");
	IApplMessage sonarDeactivate = CommUtils.buildDispatch("controller", "cmd", "deactivate", "sonar");
	IApplMessage getDistance     = CommUtils.buildRequest("controller",  "req", "getDistance", "sonar");
	IApplMessage isActive        = CommUtils.buildRequest("controller",  "req", "isActive", "sonar");

	IApplMessage activateCrtl    = CommUtils.buildDispatch("main", "cmd", "activate", "controller");

	
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
 		CommSystemConfig.protcolType    = ProtocolType.udp;
		CommSystemConfig.tracing        = false;
		ProtocolType protocol 		    = CommSystemConfig.protcolType;
		
   		radar                           = DeviceActorFactory.createRadarActor();
  		/*
  		 * IMPOSTAZIONE CABLATA
  		 * TODO: Usare annotazioni
  		 */
//		ActorJK.setActorAsRemote("led",   ctxport, host, protocol);
//		ActorJK.setActorAsRemote("sonar", ctxport, host, protocol);
   		
		ActorJK.createRemoteActors(this);
 	}
	
	protected void execute() {
		ColorsOut.outappl("UsingActorsOnPc | execute", ColorsOut.MAGENTA);
		for( int i=1; i<=2; i++) {
	 	    ActorJK.sendAMsg(turnOnLed, "led"  );
	 	    CommUtils.delay(500);
	 	    ActorJK.sendAMsg(turnOffLed, "led"  );
	 	    CommUtils.delay(500);
		}
		
		
//		new ControllerActor("controller"); //Attore locale
		ActorJK.createLocalActors(this);
		ActorJK.sendAMsg( activateCrtl );
	} 
	
//	public void terminate() {
//		System.exit(0);
//	}
	
	public static void main( String[] args) {
		BasicUtils.aboutThreads("Before start - ");
		new UsingActorsOnPc().doJob();
 		BasicUtils.aboutThreads("Before end - ");
	}

}

/*
 * Threads:
 */
