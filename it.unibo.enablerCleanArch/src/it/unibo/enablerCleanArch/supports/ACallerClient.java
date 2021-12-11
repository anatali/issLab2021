package it.unibo.enablerCleanArch.supports;

import it.unibo.enablerCleanArch.enablers.EnablerAsClient;
import it.unibo.enablerCleanArch.enablers.ProtocolType;
 

public class ACallerClient  extends EnablerAsClient{

	public ACallerClient(String name, String host, int port ) {
		super(name, host, port, ProtocolType.tcp);
	}

 

}
