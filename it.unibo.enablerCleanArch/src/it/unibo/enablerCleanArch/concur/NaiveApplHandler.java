package it.unibo.enablerCleanArch.concur;

import it.unibo.enablerCleanArch.supports.ApplMessageHandler;

public class NaiveApplHandler extends ApplMessageHandler {

	public NaiveApplHandler(String name) {
		super(name);
	}

	@Override
	public void elaborate(String message) {
		 System.out.println(name + " | elaborate " + message);
		
	}

}
