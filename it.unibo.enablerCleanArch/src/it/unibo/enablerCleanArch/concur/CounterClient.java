package it.unibo.enablerCleanArch.concur;

import it.unibo.enablerCleanArch.enablers.EnablerAsClient;
import it.unibo.enablerCleanArch.enablers.ProtocolType;
import it.unibo.enablerCleanArch.supports.Interaction2021;

public class CounterClient extends EnablerAsClient{

	public CounterClient(String name, String host, int port ) {
		super(name, host, port, ProtocolType.tcp);
	}

 
}
