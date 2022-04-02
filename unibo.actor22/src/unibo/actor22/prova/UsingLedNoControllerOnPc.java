package unibo.actor22.prova;

import it.unibo.actorComm.utils.ColorsOut;
import it.unibo.actorComm.utils.CommSystemConfig;
import it.unibo.actorComm.utils.CommUtils;
import it.unibo.kactor.IApplMessage;
import it.unibo.radarSystem22.domain.utils.BasicUtils;
import it.unibo.radarSystem22.domain.utils.DomainSystemConfig;
import unibo.actor22.Qak22Util;
 
 
/*
 * Sistema che usa led come attore locale
 */
 public class UsingLedNoControllerOnPc {
 	
 private LedActor led;
 private IApplMessage getState ;
 
	public void doJob() {
		ColorsOut.outappl("UsingLedNoControllerOnPc | Start", ColorsOut.BLUE);
		configure();
		BasicUtils.aboutThreads("Before execute - ");
		//BasicUtils.waitTheUser();
		execute();
		terminate();
	}
	
	protected void configure() {
		DomainSystemConfig.simulation   = true;			
		DomainSystemConfig.ledGui       = true;			
		DomainSystemConfig.tracing      = false;			
		
		CommSystemConfig.tracing        = false;
		
 		led = new LedActor( ApplData.ledName );
		getState = CommUtils.buildRequest("main",  "ask", ApplData.reqLedState, ApplData.ledName); 
   	}
	
	//Accende-spegne il Led due volte
	protected void execute() {
		ColorsOut.outappl("UsingLedNoControllerOnPc | execute", ColorsOut.MAGENTA);
		for( int i=1; i<=2; i++) {
	 	    Qak22Util.sendAMsg(ApplData.turnOnLed, ApplData.ledName  );
	 	    CommUtils.delay(500);
// Inviare una request richiede un attore capace di ricevere la reply	 	    
	 	    //Qak22Util.sendAMsg( getState, ApplData.ledName  );
	 	    led.elabMsg(getState);   //Richiesta asincrona. Reply inviata a main
	 	    CommUtils.delay(500); 	    
	 	    //Qak22Util.sendAMsg(ApplData.turnOffLed, ApplData.ledName  );
	 	    led.elabMsg(ApplData.turnOffLed);  //ALTERNATIVA all'uso della utility
	 	    CommUtils.delay(500);
	 	    led.elabMsg(getState);   //Richiesta asincrona. Reply inviata a main
		}
 	} 

	public void terminate() {
		BasicUtils.aboutThreads("Before exit - ");
		System.exit(0);
	}
	
	public static void main( String[] args) {
		BasicUtils.aboutThreads("Before start - ");
		new UsingLedNoControllerOnPc().doJob();
 		BasicUtils.aboutThreads("Before end - ");
	}

}

/*
 * Threads: main + Actor22 + LedGui
 */
