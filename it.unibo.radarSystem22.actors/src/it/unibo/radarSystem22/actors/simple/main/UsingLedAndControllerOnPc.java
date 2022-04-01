package it.unibo.radarSystem22.actors.simple.main;

import it.unibo.actorComm.ActorJK;
import it.unibo.actorComm.events.EventMsgHandler;
import it.unibo.actorComm.utils.BasicUtils;
import it.unibo.actorComm.utils.ColorsOut;
import it.unibo.actorComm.utils.CommSystemConfig;
import it.unibo.kactor.Actor22;
import it.unibo.kactor.IApplMessage;
import it.unibo.radarSystem22.actors.domain.LedActor;
import it.unibo.radarSystem22.domain.utils.DomainSystemConfig;
 

/*
 * Sistema che usa led e controller come attori locali
 */
 
public class UsingLedAndControllerOnPc extends Actor22{
   
	public UsingLedAndControllerOnPc(String name) {
		super(name);
 	}
	public void doJob() {
		ColorsOut.outappl("ControllerUsingLedOnPc | Start", ColorsOut.BLUE);
		configure();
		BasicUtils.aboutThreads("Before execute - ");
		//BasicUtils.waitTheUser();
		execute();
	}
	

	protected void configure() {
		DomainSystemConfig.simulation   = true;			
		DomainSystemConfig.ledGui       = true;			
		DomainSystemConfig.tracing      = false;					
		CommSystemConfig.tracing        = true;

		new UsingLedAndControllerOnPc(getName()+"Actor"); //Attivo questo programma come attore per percepire evEndWok
		new EventMsgHandler( );  //TODO: lo fa la infrastruct
		new LedActor(ApplData.ledName);
		new ControllerActorOnPc( ApplData.controllerName );
		
		//Creo altri Led per verificare che il numero di thread non aumenta
		//ATTENZIONE PERO': tutti gli attori creano un Led con GUI
		for( int i=1; i<=3; i++) {
			new LedActor(ApplData.ledName+"_"+i);
			ActorJK.sendAMsg(ApplData.turnOnLed, ApplData.ledName+"_"+i  );
			BasicUtils.delay(500);
			ActorJK.sendAMsg(ApplData.turnOffLed, ApplData.ledName+"_"+i  );
		}
		
		ActorJK.registerAsEventObserver(getName()+"Actor", ApplData.evEndWork);
  	}
	
	protected void execute() {
  		ActorJK.sendAMsg( ApplData.activateCrtl );
	} 

	public void terminate() {
		BasicUtils.aboutThreads("Before exit - ");
 	    BasicUtils.delay(3000);
		System.exit(0);
	}

	/*
	 * Percezione dell'evento di terminazione del Controller
	 */
	@Override
	protected void doJob(IApplMessage msg) {
		ColorsOut.outappl( getName()  + " | msg=" + msg  , ColorsOut.GREEN);
		terminate();
	}

	public static void main( String[] args) {
		BasicUtils.aboutThreads("Before start - ");
		new UsingLedAndControllerOnPc("main").doJob();
 	    if( ! DomainSystemConfig.ledGui ) BasicUtils.delay(3000);
 		BasicUtils.aboutThreads("Before end - ");
	}

 

}

/*
 * Threads:
 */
