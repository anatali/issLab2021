package it.unibo.actorSystem22.context;

 
import it.unibo.actorComm.interfaces.Interaction2021;
import it.unibo.actorComm.utils.BasicUtils;
import it.unibo.actorComm.utils.ColorsOut;
import it.unibo.kactor.Actor22;
import it.unibo.kactor.IApplMessage;
 

public class ActorForReply extends Actor22{
 
private Interaction2021 conn;

	public ActorForReply(String name,  Interaction2021 conn) {
		super(name);
 		this.conn = conn;
		 
	}

	@Override
	protected void doJob(IApplMessage msg) {
		BasicUtils.aboutThreads(getName()  + " |  Before doJob - ");
		ColorsOut.outappl( getName()  + " | doJob " + msg, ColorsOut.BLUE);
		if( msg.isReply() ) {
			//h.sendMsgToClient(msg.toString(), conn);		
			try {
				conn.forward( msg.toString() );
			} catch (Exception e) {
 				e.printStackTrace();
			}
		}
		//Actor22.removeActor( getName() ) o this
	}

}
