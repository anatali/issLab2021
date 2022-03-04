package it.unibo.actorComm.context;


import it.unibo.actorComm.ApplMsgHandler;
import it.unibo.actorComm.interfaces.Interaction2021;
import it.unibo.actorComm.utils.ColorsOut;
import it.unibo.kactor.IApplMessage;

public class SimpleApplHandler extends ApplMsgHandler {

	public SimpleApplHandler(String name) {
		super(name);
	}
 
	@Override
	public void elaborate( IApplMessage message, Interaction2021 conn ) {
		ColorsOut.outappl(name + " | elaborate " + message + " conn=" + conn, ColorsOut.MAGENTA);
		
	}

	@Override
	public void elaborate(String message, Interaction2021 conn) {
		System.out.println(name + " | elaborate " + message + " conn=" + conn);
		this.sendMsgToClient("answerTo_"+message, conn);
  	}

}
