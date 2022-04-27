package unibo.actor22comm.events;

import java.util.HashMap;
import java.util.Vector;

import it.unibo.kactor.ApplMessage;
import it.unibo.kactor.IApplMessage;
import kotlin.Pair;
import unibo.actor22.*;
import unibo.actor22comm.utils.ColorsOut;

/*
 * Gestisce gli eventi generati da emit locali o remote
 * Viene creato dalla prima chiamata a  Qak22Context.registerAsEventObserver
 */
public class EventMsgHandler extends QakActor22{
public static final String myName = "eventhandler";

//protected HashMap<String,String> eventObserverMap = new HashMap<String,String>();  
protected Vector<Pair<String,String>> eventObservers = new Vector<Pair<String,String>>();
 	
	public EventMsgHandler( ) {
		super(myName);
 	}

	@Override
	protected void handleMsg(IApplMessage msg) {
		ColorsOut.outappl(myName + " handles:" + msg + " ", ColorsOut.YELLOW_BACKGROUND);
		if( msg.isDispatch() && msg.msgId().equals(Qak22Context.registerForEvent)) {
			ColorsOut.outappl(myName + " register:" + msg.msgSender() + " for "+ msg.msgContent(), ColorsOut.MAGENTA);
			eventObservers.add(new Pair( msg.msgSender(),msg.msgContent() ) );			
			//eventObserverMap.put(msg.msgSender(), msg.msgContent()); //REPLACED!
		}else if( msg.isDispatch() && msg.msgId().equals(Qak22Context.unregisterForEvent)) {
			ColorsOut.outappl(myName + " unregister:" + msg.msgSender() + " for "+ msg.msgContent(), ColorsOut.MAGENTA);
			//eventObserverMap.remove(msg.msgSender(), msg.msgContent());
			//TODO
		}else if( msg.isEvent() ) {
 			updateTheObservers( msg );
		}else {
			ColorsOut.outerr(myName + " msg unknown");
		}
	}  

	protected void updateTheObservers(IApplMessage msg) {
		ColorsOut.out("updateTheObservers:" + msg + " eventObservers:" + eventObservers.size(), ColorsOut.MAGENTA); 
		
		eventObservers.forEach(
			 obs  -> {
				String actorName  = obs.getFirst();
				String evName     = obs.getSecond();
			 
				ColorsOut.out("updateTheObservers:" +actorName + " evName:" + evName, ColorsOut.MAGENTA); 
				if( evName.equals( msg.msgId()) ) {
					IApplMessage m = Qak22Util.buildEvent(msg.msgSender(), msg.msgId(), msg.msgContent(), actorName ) ;
					Qak22Util.sendAMsg( m );
					//Warning: we must declare a remote observer
				
				}
			}
		);	
	}
		
//		eventObserverMap.forEach(
//				( String actorName,  String evName) -> {
//					ColorsOut.out("updateTheObservers:" + actorName + " evName:" + evName, ColorsOut.MAGENTA); 
//					if( evName.equals( msg.msgId()) ) {
//						IApplMessage m = Qak22Util.buildEvent(msg.msgSender(), msg.msgId(), msg.msgContent(), actorName ) ;
//						Qak22Util.sendAMsg( m );
//						//Warning: we must declare a remote observer
//					}
//		} ) ;
//	}
}
