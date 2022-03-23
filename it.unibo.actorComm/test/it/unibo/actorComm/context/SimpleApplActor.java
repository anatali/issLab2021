package it.unibo.actorComm.context;



import it.unibo.actorComm.interfaces.IApplMsgHandler;
import it.unibo.actorComm.interfaces.Interaction2021;
import it.unibo.actorComm.utils.ColorsOut;
import it.unibo.kactor.ActorWrapper;
import it.unibo.kactor.IApplMessage;

 


public class SimpleApplActor extends ActorWrapper implements IApplMsgHandler{

	public SimpleApplActor(String name) {
		super(name);
	}
 
	@Override
	public void doJob( IApplMessage msg ) {
		ColorsOut.outappl( getName() + " | " + msg.toString(), ColorsOut.MAGENTA );
	}

 

	@Override
	public void elaborate(IApplMessage message, Interaction2021 conn) {
		doJob(message);
	}

	@Override
	public void sendMsgToClient(String message, Interaction2021 conn) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendAnswerToClient(String message, Interaction2021 conn) {
		// TODO Auto-generated method stub
		
	}



}
