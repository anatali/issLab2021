package it.unibo.enablerCleanArchapplHandlers;

import it.unibo.enablerCleanArch.domain.ApplMessage;
import it.unibo.enablerCleanArch.supports.ApplMsgHandler;
import it.unibo.enablerCleanArch.supports.Interaction2021;

public class NaiveApplHandler extends ApplMsgHandler {

	public NaiveApplHandler(String name) {
		super(name);
	}
 
	@Override
	public void elaborate( ApplMessage message, Interaction2021 conn ) {}

	@Override
	public void elaborate(String message, Interaction2021 conn) {
		System.out.println(name + " | elaborate " + message + " conn=" + conn);
		this.sendMsgToClient("answerTo_"+message, conn);
  	}

}
