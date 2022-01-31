package it.unibo.enablerCleanArchapplHandlers;

import java.util.HashMap;
import it.unibo.enablerCleanArch.domain.ApplMessage;
import it.unibo.enablerCleanArch.supports.ApplMsgHandler;
import it.unibo.enablerCleanArch.supports.ColorsOut;
import it.unibo.enablerCleanArch.supports.IApplMsgHandler;
import it.unibo.enablerCleanArch.supports.Interaction2021;

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

public class ContextMsgHandler extends ApplMsgHandler{
	protected HashMap<String,IApplMsgHandler> handlerMap = new HashMap<String,IApplMsgHandler>();

	public ContextMsgHandler(String name) {
		super(name);
 	}

	@Override
	public void elaborate( ApplMessage message, Interaction2021 conn ) {}

	@Override
	public void elaborate(String message, Interaction2021 conn) {
		ColorsOut.out(name+" | elaborate:" + message + " conn=" + conn);
		//msg( MSGID, MSGTYPE, SENDER, RECEIVER, CONTENT, SEQNUM )
		ApplMessage msg      = new ApplMessage(message);
		String dest          = msg.msgReceiver();
		ColorsOut.out(name +  " | elaborate " + msg.msgContent() + " dest="+dest, ColorsOut.ANSI_PURPLE);
		IApplMsgHandler h    = handlerMap.get(dest);
		//Colors.out(name +  " | elaborate " + msg.msgContent() + " redirect to handler="+h.getName() + " since dest="+dest, Colors.GREEN);
		if( dest != null && (! msg.isReply()) ) h.elaborate(msg.msgContent(), conn);	
	}

	public void addComponent( String name, IApplMsgHandler h) {
		ColorsOut.out(name +  " | added:" + name);
		handlerMap.put(name, h);
	}
	public void removeComponent( String name ) {
		ColorsOut.out(name +  " | removed:" + name);
		handlerMap.remove( name );
	}
}
