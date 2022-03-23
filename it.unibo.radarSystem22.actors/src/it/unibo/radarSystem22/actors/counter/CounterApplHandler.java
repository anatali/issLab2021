package it.unibo.radarSystem22.actors.counter;

import it.unibo.actorComm.ApplMsgHandler;
import it.unibo.actorComm.interfaces.IApplMsgHandlerForActor;
import it.unibo.actorComm.interfaces.Interaction2021;
import it.unibo.kactor.Actor22;
import it.unibo.kactor.IApplMessage;
import it.unibo.kactor.MsgUtil;
import it.unibo.radarSystem22.domain.utils.BasicUtils;
import it.unibo.radarSystem22.domain.utils.ColorsOut;

/*
 * CounterMsgHandler usa un attore CounterActor e non un POJO
 * I messaggi che gli sono passati dal TcpContextserver sono rediretti all'attore
 * che riceve l'handler al momento della costruzione.
 * L'attore esegue i messaggi (comandi/richieste) uno alla volta, anche se il
 * contatore rilascia il controllo e usa l'handler per inviare la risposta a
 * un caller remoto ( SI VEDA CounterActorCaller ).
 */
public class CounterApplHandler  extends ApplMsgHandler implements IApplMsgHandlerForActor{ 
	String decReplyTemplate = "msg( dec, reply, SENDER, RECEIVER, VALUE, 1 )";
	
	private CounterActor ca;
	private Interaction2021 conn;
	
	public CounterApplHandler( String name  ) {
		super( name );
		ca = new CounterActor("counter", this);
	}

 

	@Override
	public void elaborate(IApplMessage msg, Interaction2021 conn) {
 		ColorsOut.outappl( getName() + " | elaborate " + msg + " conn="+conn, ColorsOut.GREEN );
 		this.conn = conn;
 		Actor22.sendAMsg(msg, ca);
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
