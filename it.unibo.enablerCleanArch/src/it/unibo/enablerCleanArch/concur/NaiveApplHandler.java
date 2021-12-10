package it.unibo.enablerCleanArch.concur;

import it.unibo.enablerCleanArch.supports.ApplMessageHandler;
import it.unibo.enablerCleanArch.supports.Interaction2021;

public class NaiveApplHandler extends ApplMessageHandler {

	public NaiveApplHandler(String name) {
		super(name);
	}

	@Override
	public void elaborate(String message) {
		 System.out.println(name + " | elaborate " + message);
		
	}

	@Override
	public void elaborate(String message, Interaction2021 conn) {
		System.out.println(name + " | elaborate " + message + " conn=" + conn);
		try {
			conn.forward("Led state=");
		} catch (Exception e) {
 			e.printStackTrace();
		}
		
	}

}
