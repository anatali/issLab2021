package it.unibo.actorComm.events;

import java.util.HashMap;

import it.unibo.actorComm.ActorJK;
import it.unibo.actorComm.interfaces.IApplMsgHandler;
import it.unibo.actorComm.utils.ColorsOut;
import it.unibo.kactor.Actor22;
import it.unibo.kactor.IApplMessage;

/*
 * Gestisce gli eventi generati da emit locali o remote
 */
public class EventMsgHandler extends Actor22{
public static final String myName = "eventhandler";

protected HashMap<String,String> eventObserverMap = new HashMap<String,String>();  

 	
	public EventMsgHandler( ) {
		super(myName);
 	}

	@Override
	protected void doJob(IApplMessage msg) {
		//ColorsOut.outappl(myName + " handles:" + msg + " ", ColorsOut.MAGENTA);
		if( msg.isDispatch() && msg.msgId().equals(ActorJK.registerForEvent)) {
			ColorsOut.outappl(myName + " register:" + msg.msgSender() + " for "+ msg.msgContent(), ColorsOut.MAGENTA);
			eventObserverMap.put(msg.msgSender(), msg.msgContent());
		}else if( msg.isDispatch() && msg.msgId().equals(ActorJK.unregisterForEvent)) {
			ColorsOut.outappl(myName + " unregister:" + msg.msgSender() + " for "+ msg.msgContent(), ColorsOut.MAGENTA);
			eventObserverMap.remove(msg.msgSender(), msg.msgContent());
		}else if( msg.isEvent()) {
 			updateTheObservers( msg );
		}else {
			ColorsOut.outerr(myName + " msg unknown");
		}
	}

	protected void updateTheObservers(IApplMessage msg) {
		eventObserverMap.forEach(
				( actorName,  evName) -> {
					System.out.println("updateTheObservers:" + actorName + " evName" + evName); 
					if( evName.equals(msg.msgId()) ) {
						ActorJK.sendAMsg(msg, actorName );
					}
		} ) ;
	}
}
