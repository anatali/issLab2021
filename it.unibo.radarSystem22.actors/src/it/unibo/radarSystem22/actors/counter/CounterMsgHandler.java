package it.unibo.radarSystem22.actors.counter;

import it.unibo.actorComm.ApplMsgHandler;
import it.unibo.actorComm.interfaces.IApplMsgHandlerForActor;
import it.unibo.actorComm.interfaces.Interaction2021;
import it.unibo.kactor.IApplMessage;
import it.unibo.kactor.MsgUtil;
import it.unibo.radarSystem22.domain.utils.BasicUtils;
import it.unibo.radarSystem22.domain.utils.ColorsOut;

/*
 * 
 */
public class CounterMsgHandler  extends ApplMsgHandler implements IApplMsgHandlerForActor{ 
//	private int n = 2;
	String decReplyTemplate = "msg( dec, reply, SENDER, RECEIVER, VALUE, 1 )";
	
	private CounterActor ca;
	private Interaction2021 conn;
	
	public CounterMsgHandler( String name  ) {
		super( name );
		ca = new CounterActor("ca", this);
	}

/* 	
	protected void elabCommand( IApplMessage msg ) {
 		ColorsOut.outappl( getName() + " | elabCommand " + msg.toString(), ColorsOut.GREEN );
 		if( msg.msgId().equals("inc")) { 
 			n = n + 1; 
 		}else if( msg.msgId().equals("dec")) {
 			updateCounter( msg.msgContent() );
 		}
	}	
	
	protected void elabRequest( IApplMessage msg, Interaction2021 conn ) {
 		ColorsOut.outappl( getName() + " | elabRequest " + msg.toString(), ColorsOut.GREEN );
		if( msg.msgId().equals("dec")) {
			updateCounter( msg.msgContent() );
			String msgOutStr = decReplyTemplate
					.replace("SENDER", getName())
					.replace("RECEIVER", msg.msgSender())
					.replace("VALUE", ""+n);
			//IApplMessage  msgOut = new ApplMessage(msgOutStr);
			//Invio risposta al caller
			//MsgUtil.sendMsg(msgOut, a, null);
			sendAnswerToClient(msgOutStr,conn);
		}
	}
	
	protected void updateCounter( String dtStr ) {
		int dt = Integer.parseInt( dtStr );
		int v = n;
		v = v - 1;
		BasicUtils.delay(dt);  //the control is given to another client
		ColorsOut.outappl(getName() + " | resumes v= " + v, ColorsOut.MAGENTA);
		n = v;
		ColorsOut.outappl(getName() + " | new value after dec= " + n, ColorsOut.MAGENTA);
		BasicUtils.aboutThreads(getName() + " | CounterWithDelay after dec - ");		
	}
*/
	@Override
	public void elaborate(String msg, Interaction2021 arg1) {
 		ColorsOut.outappl( getName() + " | NEVER HERE - elaborate String" + msg, ColorsOut.GREEN );		
	}

	@Override
	public void elaborate(IApplMessage msg, Interaction2021 conn) {
 		ColorsOut.outappl( getName() + " | elaborate " + msg + " conn="+conn, ColorsOut.GREEN );
 		this.conn = conn;
 		MsgUtil.sendMsg(msg, ca, null); //null è continuation
//		if( msg.isRequest() )  elabRequest(msg,conn);
//		else if( msg.isDispatch() )  elabCommand(msg);
		
	}
	@Override
	public void sendMsgToClient(String msg) {
		sendMsgToClient(msg,conn);	
	}
	@Override
	public void sendAnswerToClient(String msg ) {
		sendAnswerToClient(msg,conn);
	}

	@Override
	public void sendAnswerToClient(String msg, Interaction2021 conn) {
 		ColorsOut.outappl( getName() + " | sendAnswerToClient " + msg+ " conn="+conn, ColorsOut.GREEN );
		try {
			conn.forward(msg);
		} catch (Exception e) {
 			e.printStackTrace();
		}
	}

	@Override
	public void sendMsgToClient(String msg, Interaction2021 conn) {
 		ColorsOut.outappl( getName() + " | sendMsgToClient " + msg+ " conn="+conn, ColorsOut.GREEN );
		try {
			conn.forward(msg);
		} catch (Exception e) {
 			e.printStackTrace();
		}		
	}


 
}
