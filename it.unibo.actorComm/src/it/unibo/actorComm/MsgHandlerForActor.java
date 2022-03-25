package it.unibo.actorComm;

import it.unibo.actorComm.interfaces.Interaction2021;
import it.unibo.kactor.Actor22;
import it.unibo.kactor.ActorBasic;
import it.unibo.kactor.IApplMessage;
import it.unibo.actorComm.utils.ColorsOut;

/*
 * L'attore passato al costruttore ha un nome
 * che viene usato nei messaggi gestiti da ContextMsgHandler
 */
public class MsgHandlerForActor extends ApplMsgHandler{
protected ActorBasic actor;
protected ActorForReply ar;
	public MsgHandlerForActor( ActorBasic actor) {
		super( actor.getName()+"H" );
		this.actor = actor;
 	}

	@Override
	public void elaborate(IApplMessage message, Interaction2021 conn) {		
		if( message.isDispatch() || message.isEvent() )  Actor22.sendAMsg(message, actor);
		else if( message.isRequest() ) {
			ColorsOut.outappl(name + " | Handling the request " + message, ColorsOut.CYAN);
			//Attivo un attore che riceve la risposta
			String actorRepyName = "ar"+message.msgSender();
			if( Actor22.getActor(actorRepyName) == null ) {
				ColorsOut.outappl(name + " | CREATE ACTOR FOR REPLY " + message, ColorsOut.MAGENTA);
				new ActorForReply(actorRepyName, this, conn);
			}
			Actor22.sendAMsg(message, actor); //invio il msg all'attore locale
		}
	}


 	public void sendMsgToClient( IApplMessage message, Interaction2021 conn  ) {
 		String destName = message.msgReceiver();
 		ActorBasic dest = Actor22.getActor(destName);
 		if( dest != null ) Actor22.sendAMsg(message, dest);
 		else ColorsOut.outerr("SORRY: destination actor not local:" + destName);
 	}
 
}
