package it.unibo.actorComm.context;

import java.util.HashMap;
import it.unibo.actorComm.ApplMsgHandler;
import it.unibo.actorComm.interfaces.IApplMsgHandler;
import it.unibo.actorComm.interfaces.IContextMsgHandler;
import it.unibo.actorComm.interfaces.Interaction2021;
import it.unibo.actorComm.utils.ColorsOut;
import it.unibo.kactor.ActorWrapper;
import it.unibo.kactor.ApplMessage;
import it.unibo.kactor.IApplMessage;
import it.unibo.kactor.MsgUtil;


/*
  * Il ContextMsgHandler viene invocato dal TcpContextServer (un singleton).
  * Esso gestisce messaggi della forma 'estesa':
 *   msg( MSGID, MSGTYPE, SENDER, RECEIVER, CONTENT, SEQNUM ) e 
 *  e ridirige al RECEIVER il CONTENT con la connessione
 *  il RECEIVER elabora il messaggio e invia un msg di risposta sulla connessione 
 *  per i messaggi che costituiscono richieste
 *  
 *  Il ContextMsgHandler potrebbe inviare al RECEIVER  il messaggio in forma estesa
 *  ma il RECEIVER non sarebbe più quello usato nella versione precedente.
 */

public class ContextMsgHandler extends ApplMsgHandler implements IContextMsgHandler{
	protected HashMap<String,IApplMsgHandler> handlerMap = new HashMap<String,IApplMsgHandler>();

 	
	public ContextMsgHandler(String name) {
		super(name);
 	}

	@Override
	public void elaborate( IApplMessage msg, Interaction2021 conn ) {
		ColorsOut.out(name+" | elaborateeeeee ApplMessage:" + msg + " conn=" + conn, ColorsOut.GREEN);
		//msg( MSGID, MSGTYPE, SENDER, RECEIVER, CONTENT, SEQNUM )
 		String dest  = msg.msgReceiver();
		//ColorsOut.out(name +  " | elaborate " + msg.msgContent() + " dest="+dest, ColorsOut.ANSI_PURPLE);
		ColorsOut.out(name +  " | elaborate  dest="+dest, ColorsOut.GREEN );
		IApplMsgHandler h    = handlerMap.get(dest);
		ColorsOut.out(name +  " | elaborate  h="+h, ColorsOut.GREEN );
		ColorsOut.out(name +  " | elaborate " + msg.msgContent() + " redirect to handler="+h.getName() + " since dest="+dest, ColorsOut.GREEN );
		if( dest != null && (! msg.isReply()) ) {
//			if( h instanceof ActorWrapper ) elaborateForActor(msg, (ActorWrapper)h);
//			else 
				h.elaborate(msg,conn);			
		}
	}

	@Override
	public void elaborate(String message, Interaction2021 conn) {
		try {
			ColorsOut.out(name+" | elaborate:" + message  , ColorsOut.GREEN);
			//ColorsOut.out(name+" | elaborate:" + message + " conn=" + conn, ColorsOut.GREEN);
			IApplMessage msg  = new ApplMessage(message);
			elaborate( msg, conn );  
		}catch(Exception e) {
			ColorsOut.outerr(name +  " | elaborate ERROR " + e.getMessage());
		}
	}

	public void addComponent( String devname, IApplMsgHandler h) {
		ColorsOut.out(name +  " | added:" + devname, ColorsOut.GREEN);
		handlerMap.put(devname, h);
	}
	public void removeComponent( String devname ) {
		ColorsOut.out(name +  " | removed:" + devname, ColorsOut.GREEN);
		handlerMap.remove( devname );
	}

	@Override
	public IApplMsgHandler getHandler(String name) {
		return handlerMap.get(name);
	}
	
//-----------------------------------------------------------------
	protected void elaborateForActor( IApplMessage msg, ActorWrapper dest ) {
		ColorsOut.outappl(name + " | elaborateForActor ApplMessage: "+msg, ColorsOut.GREEN);		
 		try {
 			if( msg.isReply() ) {
 				
 			}else {
 				//TODO usare la interfaccia IApplMsgHandler
 				MsgUtil.sendMsg(msg, dest, null);			 
 			}
		}catch( Exception e) {}	
	}

	
}
