package it.unibo.enablerCleanArch;

import it.unibo.enablerCleanArch.supports.ApplMessageHandler;
import it.unibo.enablerCleanArch.supports.Interaction2021;

public class MyHandler extends ApplMessageHandler {
private static int count = 1;

	static MyHandler create() {
		return new MyHandler( "nh"+count++);
	}
	
	private MyHandler(String name) {
		super(name);
	}
	private MyHandler(Interaction2021 conn) {
		super(conn);
	}
	public void elaborate( String message ) {
		System.out.println(name+" | elaborates: "+message);
		try {
			conn.forward("answerTo_"+message);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

