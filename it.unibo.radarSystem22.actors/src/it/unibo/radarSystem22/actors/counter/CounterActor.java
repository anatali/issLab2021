package it.unibo.radarSystem22.actors.counter;

import it.unibo.actorComm.interfaces.IApplMsgHandler;
import it.unibo.actorComm.interfaces.Interaction2021;
import it.unibo.kactor.ActorWrapper;
import it.unibo.kactor.ApplMessage;
import it.unibo.kactor.IApplMessage;
import it.unibo.kactor.MsgUtil;
import it.unibo.radarSystem22.domain.utils.BasicUtils;
import it.unibo.radarSystem22.domain.utils.ColorsOut;

/*
 * 
 */
public class CounterActor extends ActorWrapper  { 
	private CounterWithDelay counter;
	private CounterMsgHandler handler;
	
	String decReplyTemplate = "msg( dec, reply, SENDER, RECEIVER, VALUE, 1 )";
	
	public CounterActor( String name  ) {
		super( name );
		counter = new CounterWithDelay("counterWithDelay");
	}
	public CounterActor( String name, CounterMsgHandler handler  ) {
		super( name );
		this.handler = handler;
		counter = new CounterWithDelay("counterWithDelay");
	}

	@Override
	public void doJob( IApplMessage msg ) {
 		ColorsOut.outappl( getName() + " | doJob " + msg.toString(), ColorsOut.GREEN );
		if( msg.isRequest() )  elabRequest(msg, null);
		else if( msg.isDispatch() )  elabCommand(msg);
 	}
	
	protected void elabCommand( IApplMessage msg ) {
 		ColorsOut.outappl( getName() + " | elabCommand " + msg.toString(), ColorsOut.GREEN );
 		if( msg.msgId().equals("inc")) { 
 			counter.inc(); 
 		}else if( msg.msgId().equals("dec")) {
 			//updateCounter( msg.msgContent() );
 			int dt = Integer.parseInt( msg.msgContent() );
 			counter.dec( dt );  //ha delay
 		}
	}	
	
	protected void elabRequest( IApplMessage msg, Interaction2021 conn ) {
 		ColorsOut.outappl( getName() + " | elabRequest " + msg.toString(), ColorsOut.GREEN );
		if( msg.msgId().equals("dec")) {
 			int dt = Integer.parseInt( msg.msgContent() );
 			counter.dec( dt );  //ha delay
			String msgOutStr = decReplyTemplate
					.replace("SENDER", getName())
					.replace("RECEIVER", msg.msgSender())
					.replace("VALUE", ""+counter.getVal());
			ColorsOut.outappl( getName() + " | msgOutStr " + msgOutStr, ColorsOut.GREEN );
			//IApplMessage  msgOut = new ApplMessage(msgOutStr);
			//Invio risposta al caller
			//MsgUtil.sendMsg(msgOut, a, null);
			if( handler != null ) handler.sendAnswerToClient( msgOutStr );
		}
	}
	
//	protected void updateCounter( String dtStr ) {
// 		int dt = Integer.parseInt( dtStr );
////		int v = n;
////		v = v - 1;
////		BasicUtils.delay(dt);  //the control is given to another client
////		ColorsOut.outappl(getName() + " | resumes v= " + v, ColorsOut.MAGENTA);
////		n = v;
////		ColorsOut.outappl(getName() + " | new value after dec= " + n, ColorsOut.MAGENTA);
//		counter.dec( dt );  //ha delay
//		BasicUtils.aboutThreads(getName() + " | CounterWithDelay after dec - ");		
//	}

 
  
}
