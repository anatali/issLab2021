package it.unibo.enablerCleanArch.supports;

import java.util.HashMap;
import it.unibo.enablerCleanArch.domain.ApplMessage;

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
	public void elaborate(String message, Interaction2021 conn) {
		Colors.out(name+" | elaborate:" + message + " conn=" + conn, Colors.ANSI_PURPLE);
		//msg( MSGID, MSGTYPE, SENDER, RECEIVER, CONTENT, SEQNUM )
		ApplMessage msg      = new ApplMessage(message);
		String dest          = msg.msgReceiver();
		Colors.out(name +  " | elaborate " + msg.msgContent() + " dest="+dest, Colors.ANSI_PURPLE);
		IApplMsgHandler h    = handlerMap.get(dest);
		//Colors.out(name +  " | elaborate " + msg.msgContent() + " redirect to handler="+h.getName() + " since dest="+dest, Colors.GREEN);
		if( dest != null ) h.elaborate(msg.msgContent(), conn);	
	}

	public void addComponent( String name, IApplMsgHandler h) {
		Colors.out("ContextMsgHandler added:" + name);
		handlerMap.put(name, h);
	}
	public void removeComponent( String name ) {
		Colors.out("ContextMsgHandler removed:" + name);
		handlerMap.remove( name );
	}
}
