package it.unibo.comm2022.tcp;

import it.unibo.comm2022.ApplMessage;
import it.unibo.comm2022.ApplMsgHandler;
import it.unibo.comm2022.interfaces.IApplMessage;
import it.unibo.comm2022.interfaces.Interaction2021;

/*
 * Handler of messages received by the client on a connection
 * with the server
 */
class NaiveHandler extends ApplMsgHandler {
	public NaiveHandler(String name) {
		super(name);
  	}
	@Override
	public void elaborate( IApplMessage message, Interaction2021 conn ) {}

	@Override
	public void elaborate(String message, Interaction2021 conn) {
		System.out.println(name+" | elaborates: "+message);
		sendMsgToClient("answerTo_"+message, conn);	
	}
}