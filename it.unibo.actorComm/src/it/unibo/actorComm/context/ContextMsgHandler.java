package it.unibo.actorComm.context;

import java.util.HashMap;
import it.unibo.actorComm.ActorForReply;
import it.unibo.actorComm.ApplMsgHandler;
import it.unibo.actorComm.interfaces.IApplMsgHandler;
import it.unibo.actorComm.interfaces.Interaction2021;
import it.unibo.actorComm.utils.ColorsOut;
import it.unibo.kactor.Actor22;
import it.unibo.kactor.ActorBasic;
import it.unibo.kactor.IApplMessage;



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

public class ContextMsgHandler extends ApplMsgHandler implements IApplMsgHandler{ 
	protected HashMap<String,IApplMsgHandler> handlerMap = new HashMap<String,IApplMsgHandler>(); //OLD

 	
	public ContextMsgHandler(String name) {
		super(name);
 	}

	@Override
	public void elaborate( IApplMessage msg, Interaction2021 conn ) {
		ColorsOut.out(name+" | elaborateee ApplMessage:" + msg + " conn=" + conn, ColorsOut.GREEN);
		//msg( MSGID, MSGTYPE, SENDER, RECEIVER, CONTENT, SEQNUM )
		
     	if( msg.isRequest() ) {
    		String sender = msg.msgSender();
//    		Interaction2021 yetconn = requestConnectionsMap.get(sender);
//    		if( yetconn == null ) requestConnectionsMap.put(sender, conn);
			//Attivo un attore che riceve la risposta
			String actorRepyName = "ar"+msg.msgSender();
			if( Actor22.getActor(actorRepyName) == null ) {
				ColorsOut.out(name + " | CREATE ACTOR FOR REPLY " + msg, ColorsOut.BLUE);
				new ActorForReply(actorRepyName, this, conn);
			}
    	}//else { //Invia il msg all'attore
    		String receiver = msg.msgReceiver();
    		ActorBasic a = Actor22.getActor(receiver);
    		if( a != null ) {
    			Actor22.sendAMsg(msg, a);
    		}
	}
	
}
