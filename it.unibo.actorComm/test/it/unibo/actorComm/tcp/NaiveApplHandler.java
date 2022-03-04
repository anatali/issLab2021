package it.unibo.actorComm.tcp;


import it.unibo.actorComm.ApplMsgHandler;
import it.unibo.actorComm.interfaces.Interaction2021;
import it.unibo.kactor.IApplMessage;

public class NaiveApplHandler extends ApplMsgHandler {

	public NaiveApplHandler(String name) {
		super(name);
	}
 
	@Override
	public void elaborate( IApplMessage message, Interaction2021 conn ) {}

	@Override
	public void elaborate(String message, Interaction2021 conn) {
		System.out.println(name + " | elaborate " + message + " conn=" + conn);
		this.sendMsgToClient("answerTo_"+message, conn);
  	}

}
