package it.unibo.enablerCleanArchapplHandlers;

import it.unibo.enablerCleanArch.domain.ApplMessage;
import it.unibo.enablerCleanArch.supports.ApplMsgHandler;
import it.unibo.enablerCleanArch.supports.Colors;
import it.unibo.enablerCleanArch.supports.Interaction2021;
 
public class NaiveMessageHandler extends ApplMsgHandler{
 	public NaiveMessageHandler(String name) {
		super(name);
 	}

	@Override
	public void elaborate( ApplMessage message, Interaction2021 conn ) {}

	@Override
	public void elaborate(String message, Interaction2021 conn) {
		Colors.out(name + " | elaborate " + message );
 	}
	
 
}
