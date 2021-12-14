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
 *  ma il RECEIVER non sarebbe pi� quello usato nella versione precedente.
 */

public class ContextMsgHandler extends ApplMsgHandler{
	protected HashMap<String,IApplMsgHandler> handlerMap = new HashMap<String,IApplMsgHandler>();
	//protected HashMap<String,ApplMessage>    requestMap  = new HashMap<String,ApplMessage>();

	public ContextMsgHandler(String name) {
		super(name);
 	}

	@Override
	public void elaborate(String message, Interaction2021 conn) {
		ApplMessage msg      = new ApplMessage(message);
		String dest          = msg.msgReceiver();
		IApplMsgHandler h    = handlerMap.get(dest);
		Colors.out(name +  " | elaborate h="+h.getName() + " since dest="+dest);
		if( dest != null ) h.elaborate(msg.msgContent(), conn);	
	}

	public void addComponent( String name, IApplMsgHandler h) {
		handlerMap.put(name, h);
	}
	public void removeComponent( String name ) {
		handlerMap.remove( name );
	}
}