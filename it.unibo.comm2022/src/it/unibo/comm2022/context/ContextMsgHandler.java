package it.unibo.comm2022.context;

import java.util.HashMap;

import it.unibo.comm2022.ApplMessage;
import it.unibo.comm2022.ApplMsgHandler;
import it.unibo.comm2022.interfaces.IApplMessage;
import it.unibo.comm2022.interfaces.IApplMsgHandler;
import it.unibo.comm2022.interfaces.IContextMsgHandler;
import it.unibo.comm2022.interfaces.Interaction2021;
import it.unibo.comm2022.utils.ColorsOut;


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
		ColorsOut.out(name+" | elaborateeeeee ApplMessage:" + msg + " conn=" + conn, ColorsOut.BLUE);
		//msg( MSGID, MSGTYPE, SENDER, RECEIVER, CONTENT, SEQNUM )
 		String dest  = msg.msgReceiver();
		//ColorsOut.out(name +  " | elaborate " + msg.msgContent() + " dest="+dest, ColorsOut.ANSI_PURPLE);
		ColorsOut.out(name +  " | elaborate  dest="+dest, ColorsOut.BLUE );
		IApplMsgHandler h    = handlerMap.get(dest);
		ColorsOut.out(name +  " | elaborate  h="+h, ColorsOut.BLUE );
		ColorsOut.out(name +  " | elaborate " + msg.msgContent() + " redirect to handler="+h.getName() + " since dest="+dest, ColorsOut.BLUE );
		if( dest != null && (! msg.isReply()) ) h.elaborate(msg,conn);			
	}

	@Override
	public void elaborate(String message, Interaction2021 conn) {
		ColorsOut.out(name+" | elaborate:" + message + " conn=" + conn, ColorsOut.BLUE);
		try {
 			IApplMessage msg  = new ApplMessage(message);
			elaborate( msg, conn );
		}catch(Exception e) {
			ColorsOut.outerr(name +  " | elaborate ERROR " + e.getMessage());
		}
	}

	public void addComponent( String devname, IApplMsgHandler h) {
		ColorsOut.out(name +  " | added:" + devname, ColorsOut.BLUE);
		handlerMap.put(devname, h);
	}
	public void removeComponent( String devname ) {
		ColorsOut.out(name +  " | removed:" + devname, ColorsOut.BLUE);
		handlerMap.remove( devname );
	}

	@Override
	public IApplMsgHandler getHandler(String name) {
		return handlerMap.get(name);
	}
}
