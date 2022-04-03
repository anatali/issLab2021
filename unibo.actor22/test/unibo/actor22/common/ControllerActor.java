package unibo.actor22.common;
 
import it.unibo.kactor.IApplMessage;
import it.unibo.radarSystem22.domain.utils.BasicUtils;
import unibo.actor22.*;
import unibo.actor22comm.utils.ColorsOut;
import unibo.actor22comm.utils.CommUtils;

/*
 * Il controller conosce SOLO I NOMI dei dispositivi 
 * (non ha riferimenti ai dispositivi-attori)
 */
public class ControllerActor extends QakActor22{
protected int numIter = 0;
protected IApplMessage getStateRequest ;

	public ControllerActor(String name  ) {
		super(name);
		getStateRequest  = ApplData.buildRequest(name,"ask", ApplData.reqLedState, ApplData.ledName);
 	}

	@Override
	protected void handleMsg(IApplMessage msg) {  
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
//		CommUtils.aboutThreads(getName()  + " |  Before doControllerWork - ");
//		forward( ApplData.turnOffLed );
//		CommUtils.delay(500);
//		forward( ApplData.turnOnLed );
//		CommUtils.delay(500);		
// 		request(getStateRequest);
// 		forward( ApplData.turnOffLed );
		//ColorsOut.outappl( getName()  + " | numIter=" + numIter  , ColorsOut.GREEN);
   	    //Inviare un treno di messaggi NON VA BENE: mantiene il controllo del Thread degli attori  (qaksingle)
 		if( numIter++ < 3 ) {
	 	    if( numIter%2 == 1) //Qak22Util.sendAMsg(ApplData.turnOnLed, ApplData.ledName  );
	 	    	forward( ApplData.turnOnLed );
	 	    else //Qak22Util.sendAMsg(ApplData.turnOffLed, ApplData.ledName  );
	 	    	forward( ApplData.turnOffLed );
	 	    //Qak22Util.sendAMsg(getStateRequest, ApplData.ledName  );	
	 	    request(getStateRequest);
		}else {
			//Qak22Util.sendAMsg(ApplData.turnOffLed, ApplData.ledName  );
			forward( ApplData.turnOffLed );
		}
 
	}
	
	protected void elabAnswer(IApplMessage msg) {
		ColorsOut.outappl( getName()  + " | elabAnswer " + msg, ColorsOut.MAGENTA);
		CommUtils.delay(500);
		doControllerWork();
	}

}
