package it.unibo.radarSystem22.actors.counter;

 
import it.unibo.actorComm.interfaces.Interaction2021;
import it.unibo.kactor.Actor22;
import it.unibo.kactor.Actor22;
import it.unibo.kactor.IApplMessage;
import it.unibo.radarSystem22.domain.utils.ColorsOut;

/*
 * CounterActor (come Actor22) attiva doJob quando gli si manda un msg
 * Nel caso attuale può ricevere un comando dec o una richiesta dec
 * L'attore usa il POJO CounterWithDelay per eseguire il comando e la richiesta
 * Nel caso di richiesta utilizza l'handler che gli è stato dato nel costruttore
 * per inviare la risposta
 */
public class CounterActor extends Actor22  { 
	private CounterWithDelay counter;
	private CounterMsgHandler handler;
	
	String decReplyTemplate = "msg( dec, reply, SENDER, RECEIVER, VALUE, 1 )";
	
	public CounterActor( String name  ) {
		super( name );
		counter = new CounterWithDelay("counterWithDelay");
	}
	public CounterActor( String name, CounterMsgHandler handler  ) {
		super( name );
		this.handler = handler;
		counter = new CounterWithDelay("counterWithDelay");
	}

	@Override
	public void doJob( IApplMessage msg ) {
 		ColorsOut.outappl( getName() + " | doJob " + msg.toString(), ColorsOut.GREEN );
		if( msg.isRequest() )  elabRequest(msg, null);
		else if( msg.isDispatch() )  elabCommand(msg);
 	}
	
	protected void elabCommand( IApplMessage msg ) {
 		ColorsOut.outappl( getName() + " | elabCommand " + msg.toString(), ColorsOut.GREEN );
 		if( msg.msgId().equals("inc")) { 
 			counter.inc(); 
 		}else if( msg.msgId().equals("dec")) {
 			int dt = Integer.parseInt( msg.msgContent() );
 			counter.dec( dt );  //ha delay
 		}
	}	
	
	protected void elabRequest( IApplMessage msg, Interaction2021 conn ) {
 		ColorsOut.outappl( getName() + " | elabRequest " + msg.toString(), ColorsOut.GREEN );
		if( msg.msgId().equals("dec")) {
 			int dt = Integer.parseInt( msg.msgContent() );
 			counter.dec( dt );  //ha delay
			String replyStr = decReplyTemplate
					.replace("SENDER", getName())
					.replace("RECEIVER", msg.msgSender())
					.replace("VALUE", ""+counter.getVal());
			ColorsOut.outappl( getName() + " | msgOutStr " + replyStr, ColorsOut.GREEN );
 			if( handler != null ) handler.sendAnswerToClient( replyStr );
		}
	}

}
