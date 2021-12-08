package it.unibo.enablerCleanArch.concur;

import it.unibo.enablerCleanArch.enablers.EnablerAsServer;
import it.unibo.enablerCleanArch.enablers.ProtocolType;

public class EnablerCounterAsServer extends EnablerAsServer {
public static final int port = 7575;

private Counter c = new Counter();
	public EnablerCounterAsServer( String name ) {
		super(name, port, ProtocolType.tcp);
	}

	@Override
	public void elaborate(String message) {
		if( message.equals("dec(100)")) c.dec(100);	
		if( message.equals("dec(10)")) c.dec(10);
		if( message.equals("dec(0)")) c.dec(0);
	}

}
