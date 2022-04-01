package it.unibo.radarSystem22.actors.simple.main;

import it.unibo.actorComm.ActorJK;
import it.unibo.actorComm.utils.ColorsOut;
import it.unibo.actorComm.utils.CommUtils;
import it.unibo.kactor.Actor22;
import it.unibo.kactor.IApplMessage;
import it.unibo.radarSystem22.domain.utils.BasicUtils;


public class ControllerActorOnPc extends Actor22{
	
	protected int numIter = 0;
 

	public ControllerActorOnPc(String name  ) {
		super(name);
 	}

	@Override
	protected void doJob(IApplMessage msg) {  
		if( msg.isReply() ) {
			elabAnswer(msg);
		}else { 
			elabCmd(msg) ;	
		}
 	}
	
	protected void elabCmd(IApplMessage msg) {
		String msgCmd = msg.msgContent();
		ColorsOut.outappl( getName()  + " | elabCmd=" + msgCmd, ColorsOut.GREEN);
		switch( msgCmd ) {
			case ApplData.cmdActivate : {
				doControllerWork();
	 			break;
			}
			default:break;
		}		
	}
	
    protected void doControllerWork() {
		BasicUtils.aboutThreads(getName()  + " |  Before doControllerWork - ");
		//ColorsOut.outappl( getName()  + " | numIter=" + numIter  , ColorsOut.GREEN);
   	    //Inviare un treno di messaggi NON VA BENE: mantiene il controllo del Thread degli attori  (qaksingle)
		if( numIter++ < 5 ) {
	 	    if( numIter%2 == 1) ActorJK.sendAMsg(ApplData.turnOnLed, ApplData.ledName  );
	 	    else ActorJK.sendAMsg(ApplData.turnOffLed, ApplData.ledName  );
	 	    ActorJK.sendAMsg(ApplData.getState, ApplData.ledName  );	
		}else {
			ActorJK.sendAMsg(ApplData.turnOffLed, ApplData.ledName  );
			ColorsOut.outappl(getName() + " | emit " + ApplData.endWorkEvent, ColorsOut.MAGENTA);
			ActorJK.emit(ApplData.endWorkEvent);
		}
   }
	
	
	
	protected void elabAnswer(IApplMessage msg) {
		ColorsOut.outappl( getName()  + " | elabAnswer " + msg, ColorsOut.GREEN);
		CommUtils.delay(500);
		doControllerWork();
	}

}
