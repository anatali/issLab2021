package it.unibo.enablerCleanArch.supports;

import it.unibo.enablerCleanArch.enablers.ProxyAsClient;
import it.unibo.enablerCleanArch.enablers.ProtocolType;
 

public class ACallerClient  extends ProxyAsClient{

	public ACallerClient(String name, String host, String entry ) {
		super(name, host, entry, ProtocolType.tcp);
	}

 

}
