package unibo.wenvUsage22.actors;

import it.unibo.kactor.IApplMessage;
import unibo.actor22comm.utils.CommUtils;

/*
 * MESSAGGI per attori come FSM
*/	
public class SysData {
 	
	public static final String startSysCmdId = "activate";
	public static final String haltSysCmdId  = "halt";
	
	//Generali, usati dalla classe-base QakActor22Fsm
	public static final IApplMessage startSysCmd(String sender, String receiver)   {
		return CommUtils.buildDispatch(sender, startSysCmdId, "do", receiver );
	}
	public static final IApplMessage haltSysCmd(String sender, String receiver)   {
		return CommUtils.buildDispatch(sender, haltSysCmdId, "do", receiver );
	}
 
 	

}
