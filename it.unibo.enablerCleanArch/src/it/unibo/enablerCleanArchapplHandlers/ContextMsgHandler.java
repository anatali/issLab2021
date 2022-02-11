package it.unibo.enablerCleanArchapplHandlers;

import java.util.HashMap;
import it.unibo.enablerCleanArch.domain.ApplMessage;
import it.unibo.enablerCleanArch.domain.ILed;
import it.unibo.enablerCleanArch.enablers.ProtocolType;
import it.unibo.enablerCleanArch.main.RadarSystemConfig;
import it.unibo.enablerCleanArch.supports.ApplMsgHandler;
import it.unibo.enablerCleanArch.supports.ColorsOut;
import it.unibo.enablerCleanArch.supports.IApplMsgHandler;
import it.unibo.enablerCleanArch.supports.IContextMsgHandler;
import it.unibo.enablerCleanArch.supports.Interaction2021;
import it.unibo.enablerCleanArch.supports.coap.LedResourceCoap;

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
	public void elaborate( ApplMessage msg, Interaction2021 conn ) {
		ColorsOut.out(name+" | elaborateeeeee ApplMessage:" + msg + " conn=" + conn);
		//msg( MSGID, MSGTYPE, SENDER, RECEIVER, CONTENT, SEQNUM )
 		String dest  = msg.msgReceiver();
		//ColorsOut.out(name +  " | elaborate " + msg.msgContent() + " dest="+dest, ColorsOut.ANSI_PURPLE);
		ColorsOut.out(name +  " | elaborate  dest="+dest );
		IApplMsgHandler h    = handlerMap.get(dest);
		ColorsOut.out(name +  " | elaborate  h="+h );
		ColorsOut.out(name +  " | elaborate " + msg.msgContent() + " redirect to handler="+h.getName() + " since dest="+dest );
		if( dest != null && (! msg.isReply()) ) h.elaborate( msg , conn);			
	}

	@Override
	public void elaborate(String message, Interaction2021 conn) {
		ColorsOut.out(name+" | elaborate:" + message + " conn=" + conn);
		try {
 			ApplMessage msg      = new ApplMessage(message);
			elaborate( msg, conn );
	//		String dest          = msg.msgReceiver();
	//		ColorsOut.out(name +  " | elaborate " + msg.msgContent() + " dest="+dest, ColorsOut.ANSI_PURPLE);
	//		IApplMsgHandler h    = handlerMap.get(dest);
	//		ColorsOut.out(name +  " | elaborate " + msg.msgContent() + " redirect to handler="+h.getName() + " since dest="+dest, ColorsOut.GREEN);
	//		if( dest != null && (! msg.isReply()) ) h.elaborate(msg.msgContent(), conn);	
		}catch(Exception e) {
			ColorsOut.outerr(name +  " | elaborate ERROR " + e.getMessage());
		}
	}

	public void addComponent( String devname, IApplMsgHandler h) {
		ColorsOut.out(name +  " | added:" + devname);
		handlerMap.put(devname, h);
	}
	public void removeComponent( String devname ) {
		ColorsOut.out(name +  " | removed:" + devname);
		handlerMap.remove( devname );
	}

	@Override
	public IApplMsgHandler getHandler(String name) {
		return handlerMap.get(name);
	}
}
