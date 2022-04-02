package unibo.actor22.prova;

import it.unibo.actorComm.events.EventMsgHandler;
import it.unibo.actorComm.utils.BasicUtils;
import it.unibo.actorComm.utils.ColorsOut;
import it.unibo.actorComm.utils.CommSystemConfig;
import it.unibo.kactor.IApplMessage;
import it.unibo.radarSystem22.domain.utils.DomainSystemConfig;
import unibo.actor22.*;
 

/*
 * Sistema che usa led e controller come attori locali
 */
 
public class UsingLedAndControllerOnPc {
   

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
		CommSystemConfig.tracing        = false;

		new LedActor(ApplData.ledName);
		new ControllerActor( ApplData.controllerName );
		
		//Creo altri Led per verificare che il numero di thread non aumenta
//		for( int i=1; i<=3; i++) {
//			new LedActor(ApplData.ledName+"_"+i);
//			Qak22Util.sendAMsg(ApplData.turnOnLed, ApplData.ledName+"_"+i  );
//			BasicUtils.delay(500);
//			Qak22Util.sendAMsg(ApplData.turnOffLed, ApplData.ledName+"_"+i  );
//		}
  	}
	
	protected void execute() {
  		Qak22Util.sendAMsg( ApplData.activateCrtl );
	} 

	public void terminate() {
		BasicUtils.aboutThreads("Before exit - ");
 	    BasicUtils.delay(3000);
		System.exit(0);
	}


	public static void main( String[] args) {
		BasicUtils.aboutThreads("Before start - ");
		new UsingLedAndControllerOnPc().doJob();
 		BasicUtils.aboutThreads("Before end - ");
	}

 

}

/*
 * Threads:
 */
