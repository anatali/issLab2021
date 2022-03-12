package it.unibo.radarSystem22.actors.experimentQak;

import it.unibo.actorComm.utils.ColorsOut;
import it.unibo.kactor.Actor22;
import it.unibo.kactor.ActorBasic;
import it.unibo.kactor.IApplMessage;
import it.unibo.kactor.MsgUtil;
import it.unibo.radarSystem22.actors.domain.support.DeviceLang;
import it.unibo.radarSystem22.actors.domain.support.DomainData;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.CoroutineScopeKt;
import kotlinx.coroutines.ExecutorCoroutineDispatcher;
import static kotlinx.coroutines.ThreadPoolDispatcherKt.newSingleThreadContext; 


/*
 * Controller di prova per il led qak
 */
public class LedControllerActorQak extends ActorBasic{  
    private static CoroutineScope createScope(){
        ExecutorCoroutineDispatcher d = newSingleThreadContext("single");
        CoroutineScope scope = CoroutineScopeKt.MainScope();
        return scope;
    } 
	
 /*
 * Costruttore che riceve i riferimenti agli attori componenti il sistema
 */
	public LedControllerActorQak( String name ) {
		super(name,createScope(), false, true, false, 50);
 		IApplMessage controllerActivate   = 
				MsgUtil.buildDispatch("main", DeviceLang.cmd, "activate", name);
		Actor22.sendMsg( controllerActivate, this);
		//this.autoMsg(DeviceLang.cmd, "activate", null);  //
 	}
	
	@Override
	public Object actorBody(IApplMessage msg, Continuation<? super Unit> arg1) {
		doJob(msg);
 		return null; 
	}
	
 
	protected void doJob(IApplMessage msg) { 
 		String msgId = msg.msgId();
		switch( msgId ) {
			case DeviceLang.cmd  : elabCmd(msg);break;
 		    default: ColorsOut.outerr(getName()  + " | unknown " + msgId);
		}		
  	}
	
	protected void elabCmd(IApplMessage msg) {
		String msgCmd = msg.msgContent();
		ColorsOut.outappl( getName()  + " | elabCmd " + msgCmd, ColorsOut.GREEN);
		switch( msgCmd ) {
			case "activate" : {
				//Actor22.sendMsg(DomainData.led, led );
				this.forward(DeviceLang.cmd, "turnOn", "led", null);
 				break;
			}
 		}		
	}

	
 	
	
	
 
}
