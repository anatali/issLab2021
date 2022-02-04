package it.unibo.enablerCleanArch.tcp;

import it.unibo.enablerCleanArch.domain.ApplMessage;
import it.unibo.enablerCleanArch.supports.ApplMsgHandler;
import it.unibo.enablerCleanArch.supports.Interaction2021;

/*
 * Handler of messages received by the client on a connection
 * with the server
 */
class NaiveHandler extends ApplMsgHandler {
	public NaiveHandler(String name) {
		super(name);
  	}
	@Override
	public void elaborate( ApplMessage message, Interaction2021 conn ) {}

	@Override
	public void elaborate(String message, Interaction2021 conn) {
		System.out.println(name+" | elaborates: "+message);
		sendMsgToClient("answerTo_"+message, conn);	
	}
}