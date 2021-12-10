package it.unibo.enablerCleanArch;

import it.unibo.enablerCleanArch.supports.ApplMessageHandler;
import it.unibo.enablerCleanArch.supports.Colors;
import it.unibo.enablerCleanArch.supports.Interaction2021;

public class MyHandler extends ApplMessageHandler {

	//static MyHandler create() { return new MyHandler( "nh"+count++); }
	
	//private MyHandler(String name) { super(name); }
	//public MyHandler(Interaction2021 conn) {super(conn);}

	public void elaborate( String message ) {
		System.out.println(name+" | elaborates: "+message);
		try {
			conn.forward("answerTo_"+message);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void elaborate(String message, Interaction2021 conn) {
		// TODO Auto-generated method stub
		
	}
}

