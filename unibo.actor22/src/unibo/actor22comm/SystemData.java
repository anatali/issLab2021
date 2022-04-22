package unibo.actor22comm;

import it.unibo.kactor.IApplMessage;
import unibo.actor22.Qak22Util;
import unibo.actor22comm.utils.CommUtils;

/*
 * MESSAGGI per attori come FSM
*/	
public class SystemData {

	public static final String wsEventId     = "wsEvent";

	public static final String startSysCmdId = "activate";
	public static final String haltSysCmdId  = "halt";
	
	//Generali, usati dalla classe-base QakActor22Fsm
	public static final IApplMessage startSysCmd(String sender, String receiver)   {
		return CommUtils.buildDispatch(sender, startSysCmdId, "do", receiver );
	}
	public static final IApplMessage haltSysCmd(String sender, String receiver)   {
		return CommUtils.buildDispatch(sender, haltSysCmdId, "do", receiver );
	}
	public static final IApplMessage startSysRequest(String sender, String receiver)   {
		return CommUtils.buildRequest(sender, startSysCmdId, "do", receiver );
	}
	public static final IApplMessage sysRequestRepy(String sender, String receiver)   {
		//CommUtils.prepareReply(requestMsg, receiver)
		return CommUtils.buildReply(sender, startSysCmdId, "done", receiver );
	}
 
	public static final String activateActorCmd = "activateActor";
	public static final  IApplMessage activateActor(String sender, String receiver) {
		return Qak22Util.buildDispatch(sender, activateActorCmd, "do", receiver);
	}
 	

}
