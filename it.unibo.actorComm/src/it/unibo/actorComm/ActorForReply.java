package it.unibo.actorComm;

import it.unibo.actorComm.interfaces.IApplMsgHandler;
import it.unibo.actorComm.interfaces.Interaction2021;
import it.unibo.actorComm.utils.BasicUtils;
import it.unibo.actorComm.utils.ColorsOut;
import it.unibo.kactor.Actor22;
import it.unibo.kactor.IApplMessage;
 

public class ActorForReply extends Actor22{
private IApplMsgHandler h;
private Interaction2021 conn;

	public ActorForReply(String name, IApplMsgHandler h, Interaction2021 conn) {
		super(name);
		this.h = h;
		this.conn = conn;
		 
	}

	@Override
	protected void doJob(IApplMessage msg) {
		BasicUtils.aboutThreads(getName()  + " |  Before doJob - ");
		ColorsOut.outappl( getName()  + " | doJob " + msg, ColorsOut.BLUE);
		if( msg.isReply() ) h.sendAnswerToClient(msg.toString(), conn);		
	}

}
