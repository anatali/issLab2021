package it.unibo.radarSystem22.actors.businessLogic;

import it.unibo.actorComm.ActorJK;
import it.unibo.actorComm.utils.ColorsOut;
import it.unibo.actorComm.utils.CommUtils;
import it.unibo.kactor.Actor22;
import it.unibo.kactor.IApplMessage;
import it.unibo.radarSystem22.actors.domain.support.DomainData;

/*
 * Attore necessario per permettere al Controller di ciclare
 */
public class SonarCallerActor extends Actor22{
	
private IApplMessage curRequest;

  	public SonarCallerActor(String name ) {
		super(name);
 	}
 
	@Override
	protected void doJob(IApplMessage msg) { 
		//ColorsOut.outappl( getName()  + " | doJob=" + msg, ColorsOut.GREEN);
		if( msg.isReply() ) {
			elabAnswer(msg);
		}else { 
			elabRequest(msg);
		}
		
 	}
	
	protected void elabRequest(IApplMessage msg) {
		String msgCmd = msg.msgContent();
		//ColorsOut.outappl( getName()  + " | elabCmd=" + msgCmd, ColorsOut.GREEN);
		switch( msgCmd ) {
			case "activate" : {
				curRequest = msg;
				//NOTARE che si può inivare la risposta direttamente al Caller simulando che sia lui il Sender!!!!
				ActorJK.sendAMsg( DomainData.sonarDistance ); //Manda la risposta al Controller !!!
				//ActorJK.sendAMsg( DomainData.getsonarDistance ); //Chiedo la distanza
 				break;
			}
		}
	}
 
	//Eseguito se ActorJK.sendAMsg( DomainData.getsonarDistance )
	protected void elabAnswer(IApplMessage msg) {
 		//SONAR DATA
		String answer = msg.msgContent();
		int curDistance   = Integer.parseInt(answer);
		ColorsOut.outappl( getName()  + " | elabAnswer =" + curDistance, ColorsOut.MAGENTA);
		IApplMessage reply =  CommUtils.prepareReply(curRequest, answer);
 		ActorJK.sendReply( curRequest, reply);		
	}

}
